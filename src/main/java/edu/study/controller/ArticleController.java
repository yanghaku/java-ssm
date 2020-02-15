package edu.study.controller;

import edu.study.dao.*;
import edu.study.model.*;
import edu.study.util.recommend.ContentBasedRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "article")
public class ArticleController {

    @Autowired
    ArticleCategoryMapper articleCategoryService;

    @Autowired
    ArticleMapper articleService;

    @Autowired
    CommentMapper commmentService;

    @Autowired
    ArticleAgreeMapper articleAgreeService;

    @Autowired
    ArticleCollectionMapper articleCollectionService;

    @Autowired
    ArticleRecommendMapper articleRecommendService;


    /**
     *
     * 文章分类相关
     * 三个服务： 获取文章分类列表，获取某一分类的文章数，获取某一分类的文章列表
     * type有三种： view：浏览全部， own： 浏览用户发表的， collect： 浏览用户收藏的
     * 如果参数里面指定用户，就用指定的用户，否则就默认当前登录的用户
     */

    // 获取所有文章分类
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseBody
    public List<ArticleCategory> getCategory(){
        return articleCategoryService.selectAll();
    }

    // 获取某一分类的文章数
    @RequestMapping(value = "/articleCount", method = RequestMethod.GET)
    @ResponseBody
    public Integer articleCountByCategoryId(HttpServletRequest request,
            @RequestParam Integer categoryId, @RequestParam String type,
                        @RequestParam(required = false) String username){

        if(type == null)return 0;
        if(type.equals("view"))
            return articleService.articleCountByCategoryId(categoryId);
        if(username == null){
            username = (String)request.getSession().getAttribute("current_user");
            if(username == null)return 0;
        }
        if(type.equals("collect")){
            return articleService.articleCountByCategoryIdCollect(categoryId,username);
        }
        else if(type.equals("own")){
            return articleService.articleCountByCategoryIdUsername(categoryId,username);
        }
        else return 0;//不合法
    }

    // 获取某一分类的文章列表
    @RequestMapping(value = "/articleList", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> getArticleListByPageLimit(HttpServletRequest request,
            @RequestParam Integer categoryId, @RequestParam Integer start,
            @RequestParam Integer size,@RequestParam String type,
             @RequestParam(required = false)String username){

        HashMap<String,Object> mp=new HashMap<>();
        mp.put("categoryId",categoryId);
        mp.put("start",start);
        mp.put("size",size);

        if(type == null)return null;
        if(type.equals("view")) {// 如果是浏览全部，就直接查询
            return articleService.selectByPageLimit(mp);
        }

        if(username == null){
            username = (String)request.getSession().getAttribute("current_user");
            if(username == null)return null;
        }
        mp.put("username",username);

        if(type.equals("own")){
            return articleService.selectByPageLimitUsername(mp);
        }
        else if(type.equals("collect")){
            return articleService.selectByPageLimitCollect(mp);
        }
        else return null;
    }








    /**
     *
     * 文章本身相关
     * 三个服务： 获取某一文章详细信息，增加新的文章，更新文章
     */

    // 获取某一个文章的详细信息
    @RequestMapping(value = "/getSingleArticle", method = RequestMethod.GET)
    @ResponseBody
    public Article getArticle(HttpServletRequest request, @RequestParam Integer articleId){
        if(request.getSession().getAttribute("current_user") == null)return null;
        // 点击量加一
        articleService.updateArticleClicks(articleId);
        return articleService.selectByPrimaryKey(articleId);
    }

    // 增加新的文章
    @RequestMapping(value = "/insertArticle", method = RequestMethod.POST)
    @ResponseBody
    public Integer insertArticle(HttpServletRequest request, @RequestBody Article article){
        article.setUsername((String)request.getSession().getAttribute("current_user"));
        if(article.getUsername() == null) return -1;
        article.setClicks(0);
        article.setCreateTime(new Date());
        article.setModifyTime(new Date());
        if(articleService.insertAndGetId(article) == 1){
            return article.getArticleId();
        }
        return -1;
    }

    // 更新文章
    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    @ResponseBody
    public Integer updateArticle(HttpServletRequest request, @RequestBody Article article){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || !username.equals(article.getUsername())) return -1;
        article.setModifyTime(new Date());
        if(articleService.updateByPrimaryKey(article) == 1){
            return article.getArticleId();
        }
        return -1;
    }








    /**
     * 文章评论相关：
     * 三个服务：获取文章所有评论，文章增加评论，获取文章评论个数
     *
     */

    // 获取文章的所有评论
    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    @ResponseBody
    public List<Comment> getComment(HttpServletRequest request,@RequestParam Integer articleId){
        if(request.getSession().getAttribute("current_user") == null)return null;
        return commmentService.selectByArticleId(articleId);
    }

    // 文章增加评论
    @RequestMapping(value = "/insertComment", method = RequestMethod.POST)
    @ResponseBody
    public Comment insertComment(HttpServletRequest request,@RequestBody Comment comment){
        comment.setUsername((String)request.getSession().getAttribute("current_user"));
        if(comment.getUsername() == null)return null;
        comment.setCreateTime(new Date());
        if( commmentService.insertAndGetId(comment) == 1) return comment;
        return null;
    }

    // 获取文章的评论个数
    @RequestMapping(value = "/commentCount", method = RequestMethod.GET)
    @ResponseBody
    public Integer commentCount(@RequestParam Integer articleId){
        return commmentService.countByArticleId(articleId);
    }







    /**
     * 文章收藏相关
     * 三个服务：获取文章收藏个数，用户是否收藏文章，增加用户收藏文章
     */

    // 获取文章的收藏个数
    @RequestMapping(value = "/articleCollectCount",method = RequestMethod.GET)
    @ResponseBody
    public Integer articleCollectCount(@RequestParam Integer articleId){
        return articleCollectionService.countByArticleId(articleId);
    }

    // 获取
    // 用户是否收藏文章
    @RequestMapping(value = "/getArticleCollect", method = RequestMethod.GET)
    @ResponseBody
    public Integer getArticleCollect(HttpServletRequest request, @RequestParam Integer articleId){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || articleCollectionService.selectByPrimaryKey(username,articleId) == null )
            return 0;
        return 1;
    }

    // 增加用户收藏文章
    @RequestMapping(value = "/insertArticleCollect", method = RequestMethod.GET)
    @ResponseBody
    public Integer insertArticleCollect(HttpServletRequest request, @RequestParam Integer articleId){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null) return 0;
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setUsername(username);
        articleCollection.setArticleId(articleId);
        articleCollection.setTime(new Date());
        return articleCollectionService.insert(articleCollection);
    }





    /**
     * 文章点赞相关
     * 三个服务：获取文章点赞个数，用户是否点赞文章，增加用户点赞文章
     */

    // 获取文章的点赞个数
    @RequestMapping(value = "/articleAgreeCount",method = RequestMethod.GET)
    @ResponseBody
    public Integer articleAgreeCount(@RequestParam Integer articleId){
        return articleAgreeService.countByArticleId(articleId);
    }

    // 获取用户是否点赞 某篇文章
    @RequestMapping(value = "/getArticleAgree", method = RequestMethod.GET)
    @ResponseBody
    public Integer getArticleAgree(HttpServletRequest request, @RequestParam Integer articleId){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || articleAgreeService.selectByPrimaryKey(username,articleId) == null )
            return 0;
        return 1;
    }

    // 增加用户点赞文章
    @RequestMapping(value = "/insertArticleAgree", method = RequestMethod.GET)
    @ResponseBody
    public Integer insertArticleAgree(HttpServletRequest request, @RequestParam Integer articleId){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null) return 0;
        ArticleAgree articleAgree = new ArticleAgree();
        articleAgree.setUsername(username);
        articleAgree.setArticleId(articleId);
        articleAgree.setTime(new Date());
        return articleAgreeService.insert(articleAgree);
    }





    /**
     *  文章推荐
     */
    @RequestMapping(value = "/getArticleRecommend", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> getArticleRecommend(HttpServletRequest request){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null)return null;
        List<Article> res =new ArrayList<>();
        Object attr = request.getSession().getAttribute("articleRecommend");
        if(attr == null){
            List<Integer> article_ids = articleRecommendService.selectByUsername(username);
            Set<Integer> set = new HashSet<>();// 记录已经添加的articleId（去重）
            for(Integer article_id: article_ids){
                Article article = articleService.selectByPrimaryKey(article_id);
                if(article != null){
                    res.add(article);
                    set.add(article.getArticleId());
                }
            }
            // 如果推荐的个数不到规定的个数, 就获取点击量最多的文章来补充
            if(res.size()< ContentBasedRecommend.RECOMMEND_NUM){
                List<Article> articles = articleService.selectByclicks(ContentBasedRecommend.RECOMMEND_NUM);
                for(Article article:articles){
                    if(set.contains(article.getArticleId()))continue;
                    res.add(article);
                    set.add(article.getArticleId());
                    if(res.size() >= ContentBasedRecommend.RECOMMEND_NUM)break;
                }
            }
            request.getSession().setAttribute("articleRecommend", res);// 保存在session里
        }
        else if(attr instanceof List<?>) {
            for(Object obj: (List<?>)attr){
                res.add((Article) obj);
            }
        }
        return res;
    }


    @RequestMapping(value = "/countArticleByAuthor", method = RequestMethod.GET)
    @ResponseBody
    Integer countArticleByAuthor(@RequestParam String username){
        if(username==null)return 0;
        return articleService.countByAuthor(username);
    }

    @RequestMapping(value = "/countArticleByCollect", method = RequestMethod.GET)
    @ResponseBody
    Integer countArticleByCollect(@RequestParam String username){
        if(username == null)return 0;
        return articleService.countByCollect(username);
    }

}
