package edu.study.util.recommend;


import edu.study.dao.*;
import edu.study.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContentBasedRecommend {

    // 需要用到的数据库服务
    @Autowired
    ArticleMapper articleService;

    @Autowired
    ArticleAgreeMapper articleAgreeService;

    @Autowired
    ArticleCollectionMapper articleCollectionService;

    @Autowired
    ArticleKeywordMapper articleKeywordService;

    @Autowired
    UserKeywordMapper userKeywordService;

    @Autowired
    ArticleRecommendMapper articleRecommendService;

    private Date lastRefreshDate;//维护上次更新的时间

    private static final int ARTICLE_KEYWORD_MAX_NUM = 100;// 设置一个文章的关键词上限

    private static final int USER_KEYWORD_MAX_NUM = 1000;//设置存储的用户关键词的上限

    public static final int RECOMMEND_NUM = 20; // 设置每个用户推荐的文章个数

    private static final double AUTO_DEC_NUM = 0.9; //设置每天衰减的系数

    private static final double lambda = 1.5;   //设置收藏的权重 对于 点赞的权重的倍数

    /**
     *根据lastrefreshTime ~ 至今 这个时间段的活动记录，刷新推荐
     * 并将lastrefreshTime 更新到现在
     *
     */
    public void refresh(){
        if(lastRefreshDate == null){//如果上次更新时间没有实例化，那就创建一个最早的时间
            lastRefreshDate = new Date();
            lastRefreshDate.setTime(0);
        }
        System.err.println("last time: " + lastRefreshDate);
        Date nowRefreshTime = new Date();//本次的更新时间
        System.err.println("now: "+ nowRefreshTime);


        // 1. 将时间段内更新的文章的关键词更新
        articleKeywordRefresh();

        // 2. 根据两次时间差，将所有用户的喜好衰减更新
        userKeywordAutoDec(lastRefreshDate,nowRefreshTime);

        // 3. 根据时间段内的活动记录，将活动的用户的关键词更新
        HashMap<String,HashMap<String,Double> > usersKeyword =  userKeywordRefresh();
        if(usersKeyword.size() == 0){// 如果这段时间没有产生用户行为，就直接退出
            lastRefreshDate = nowRefreshTime;
            return;
        }

        List<ArticleKeyword> articleKeywordList = articleKeywordService.selectAll();
        // 预处理所有的文章的关键词保存下来
        HashMap<Integer,List<Keyword> >articleKeyword = new HashMap<>();
        for(ArticleKeyword item : articleKeywordList){
            if(articleKeyword.containsKey(item.getArticleId())){
                articleKeyword.get(item.getArticleId()).add(new Keyword(item.getKeyword(),item.getTfidf()));
            }
            else{
                List<Keyword> keywordList = new ArrayList<>();
                keywordList.add(new Keyword(item.getKeyword(),item.getTfidf()));
                articleKeyword.put(item.getArticleId(),keywordList);
            }
        }
        articleKeywordList.clear();

        // 预处理出来 每个关键词列表的 平方和的平方根 （余弦定理的分母部分）
        HashMap<String,Double> userDiv = new HashMap<>();
        HashMap<Integer,Double> articleDiv = new HashMap<>();
        for(Map.Entry<String,HashMap<String,Double>> entry: usersKeyword.entrySet()){
            double sum=0;
            for(Double val: entry.getValue().values()){
                sum += val*val;
            }
            userDiv.put(entry.getKey(),Math.sqrt(sum));
        }
        for(Map.Entry<Integer,List<Keyword> > entry: articleKeyword.entrySet()){
            double sum=0;
            for(Keyword x : entry.getValue()){
                sum += x.getTfidf() * x.getTfidf();
            }
            articleDiv.put(entry.getKey(),Math.sqrt(sum));
        }


        List<ArticleRecommend> articleRecommends = new ArrayList<>();
        //开始推荐计算
        for(Map.Entry<String,HashMap<String,Double> > userEntry: usersKeyword.entrySet()){
            //外层循环遍历每个用户
            articleRecommends.clear();
            for(Map.Entry<Integer,List<Keyword>> articleEntry: articleKeyword.entrySet()){
                //内层循环遍历每篇文章
                double score = similarity(userEntry.getValue(),articleEntry.getValue());
                if(score > 0.00001){// 非零就加入推荐列表里
                    score /= (userDiv.get(userEntry.getKey()) * articleDiv.get(articleEntry.getKey()));
                    articleRecommends.add(new ArticleRecommend(userEntry.getKey(),articleEntry.getKey(),score));
                }
            }

            // 删除数据库中原先的推荐信息
            articleRecommendService.deleteByUsername(userEntry.getKey());

            // 如果推荐的列表大于需要推荐的个数，就排序，添加前面推荐值高的
            // 保存到数据库中

            if(articleRecommends.isEmpty())continue;

            if(articleRecommends.size() > RECOMMEND_NUM){
                sortRecommendList(articleRecommends);
                articleRecommendService.insertList(articleRecommends.subList(0,RECOMMEND_NUM));
            }
            else articleRecommendService.insertList(articleRecommends);
        }

        // 最后将lastRefreshTime 更新成本次更新的时间
        lastRefreshDate = nowRefreshTime;
    }


    /**
     * 将lastrefreshTime ~ 至今 时间段的文章的关键词更新
     */
    private void articleKeywordRefresh(){
        List<Article> articleList = articleService.selectByModifyTime(lastRefreshDate);
        for(Article article : articleList){
            // 对于每个文章，分析出关键词列表
            List<Keyword> keywordList = TFIDF.getTFIDF(article.getTitle(),article.getContent(),ARTICLE_KEYWORD_MAX_NUM,articleService);
            // 使用sql批量修改
            if(!keywordList.isEmpty())articleKeywordService.replaceInto(article.getArticleId(),keywordList);
        }
    }


    /**
     *根据规定时间段的活动记录，将用户的关键词更新
     * 并且将涉及到的用户更新后的值返回（以便为这些用户推荐）
     */

    private HashMap<String,HashMap<String,Double> > userKeywordRefresh(){
        // 分为两部分：收藏的文章 和 点赞的文章
        // 其中收藏的文章的权重为点赞的文章的 lambda 倍
        
        HashMap<String,HashMap<String,Double> > record = new HashMap<>();
        // 记录涉及到的每个用户的keyword表，最后一起更新到数据库
        // 处理过程中keyword 保存为hashmap ，便于合并（降低查找复杂度）

        // 1. 文章收藏
        List<ArticleCollection> articleCollectionList = articleCollectionService.selectByTime(lastRefreshDate);
        if(articleCollectionList != null) {
            for (ArticleCollection articleCollection : articleCollectionList) {
                List<Keyword> articleKeywordList = articleKeywordService.selectByArticleId(articleCollection.getArticleId());
                if(articleKeywordList == null || articleKeywordList.isEmpty())continue;
                if (record.containsKey(articleCollection.getUsername())) {
                    // 如果已经有了就合并
                    HashMap<String,Double> tmp = record.get(articleCollection.getUsername());
                    for(Keyword articleKeyword: articleKeywordList){
                        if(tmp.containsKey(articleKeyword.getKeyword())) {
                            tmp.put(articleKeyword.getKeyword(),
                                    tmp.get(articleKeyword.getKeyword()) + articleKeyword.getTfidf() * lambda);
                        }
                        else tmp.put(articleKeyword.getKeyword(),articleKeyword.getTfidf()*lambda);

                    }
                    record.put(articleCollection.getUsername(), tmp);
                }
                else{
                    HashMap<String,Double> tmp = new HashMap<>();
                    for(Keyword articleKeyword: articleKeywordList){
                        tmp.put(articleKeyword.getKeyword(),articleKeyword.getTfidf()*lambda);
                    }
                    record.put(articleCollection.getUsername(), tmp);
                }
            }
        }

        // 2. 文章点赞
        List<ArticleAgree> articleAgreeList = articleAgreeService.selectByTime(lastRefreshDate);
        if(articleAgreeList != null) {
            for (ArticleAgree articleAgree : articleAgreeList) {
                List<Keyword> articleKeywordList = articleKeywordService.selectByArticleId(articleAgree.getArticleId());
                if(articleKeywordList == null || articleKeywordList.isEmpty())continue;
                if (record.containsKey(articleAgree.getUsername())) {
                    // 如果已经有了就合并
                    HashMap<String,Double> tmp = record.get(articleAgree.getUsername());
                    for(Keyword articleKeyword: articleKeywordList){
                        if(tmp.containsKey(articleKeyword.getKeyword())) {
                            tmp.put(articleKeyword.getKeyword(),
                                    tmp.get(articleKeyword.getKeyword()) + articleKeyword.getTfidf());
                        }
                        else tmp.put(articleKeyword.getKeyword(),articleKeyword.getTfidf());
                    }
                    record.put(articleAgree.getUsername(), tmp);
                }
                else{
                    HashMap<String,Double> tmp = new HashMap<>();
                    for(Keyword articleKeyword: articleKeywordList){
                        tmp.put(articleKeyword.getKeyword(),articleKeyword.getTfidf());
                    }
                    record.put(articleAgree.getUsername(), tmp);
                }
            }
        }

        // 将增加的关键词 与 用户原先的合并
        // 然后将更新的键值 保存到数据库中
        for(HashMap.Entry<String,HashMap<String,Double> > entry : record.entrySet()){
            List<Keyword> userKeywordList = userKeywordService.selectByUsername(entry.getKey());
            if(userKeywordList != null) {
                for (Keyword keyword : userKeywordList) {
                    // 如果已经有就合并
                    if (entry.getValue().containsKey(keyword.getKeyword())) {
                        entry.getValue().put(keyword.getKeyword(),
                                entry.getValue().get(keyword.getKeyword()) + keyword.getTfidf());
                    } else entry.getValue().put(keyword.getKeyword(), keyword.getTfidf());
                }
            }
            //将每个用户对应的关键词的map 以列表的形式存下来(方便插入数据库)
            if(userKeywordList != null )userKeywordList.clear();
            else userKeywordList = new ArrayList<>();
            for(HashMap.Entry<String,Double> keyWordEntry: entry.getValue().entrySet()){
                userKeywordList.add(new Keyword(keyWordEntry.getKey(),keyWordEntry.getValue()));
            }
            if(userKeywordList.isEmpty())continue;
            if(userKeywordList.size() > USER_KEYWORD_MAX_NUM) {// 如果超过了规定的数，就删除影响最小的
                sortKeyWordList(userKeywordList);
                while (userKeywordList.size() > USER_KEYWORD_MAX_NUM){
                    entry.getValue().remove(userKeywordList.get(userKeywordList.size()-1).getKeyword());
                    userKeywordList.remove(userKeywordList.size()-1);
                }
            }

            // 最后将此保存到数据库中
            userKeywordService.replaceInto(entry.getKey(),userKeywordList);
        }

        return record;
    }


    /**
     * 将关键词列表从大到小排序
     * @param keywordList 关键词列表
     */
    private void sortKeyWordList(List<Keyword> keywordList){
        Collections.sort(keywordList, new Comparator<Keyword>() {// 使用内部类自定义排序规则
            @Override
            public int compare(Keyword o1, Keyword o2) {
                // 按照TFIDF值从大到小排序
                if(o2.getTfidf() - o1.getTfidf() > 0)return 1;
                else if(o2.getTfidf() - o1.getTfidf() == 0)return 0;
                return -1;
            }
        });
    }

    /**
     * 排序
     * @param articleRecommendList 推荐列表
     */
    private void sortRecommendList(List<ArticleRecommend> articleRecommendList){
        Collections.sort(articleRecommendList, new Comparator<ArticleRecommend>() {
            @Override
            public int compare(ArticleRecommend o1, ArticleRecommend o2) {
                if(o2.getSimilarity() - o1.getSimilarity() > 0)return 1;
                else if(o2.getSimilarity() - o1.getSimilarity() == 0)return 0;
                return -1;
            }
        });
    }

    /**
     *
     * @param hashMap 用户的关键词表
     * @param keywordList 文章的关键词表
     * @return 返回两者的相似度 (余弦定理的上半部分)
     */
    private double similarity(HashMap<String,Double> hashMap,List<Keyword> keywordList){
        double ans=0;
        for(Keyword keyword: keywordList){
            if(hashMap.containsKey(keyword.getKeyword())){
                ans += keyword.getTfidf() * hashMap.get(keyword.getKeyword());
            }
        }
        return ans;
    }


    /**
     *  用户的兴趣随时间衰减值
     */
    public void userKeywordAutoDec(Date from,Date to){
        double day = to.getTime() - from.getTime();
        day /= (1000 * 60 * 60 * 24 ); //两者相隔的天数
        double dec = Math.pow(AUTO_DEC_NUM , day);
        userKeywordService.mulAll(dec);
    }
}
