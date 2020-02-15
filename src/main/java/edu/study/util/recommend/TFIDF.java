package edu.study.util.recommend;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.study.dao.ArticleMapper;
import edu.study.model.Keyword;

import java.util.*;

public class TFIDF {

    public static List<Keyword> getTFIDF(String title, String content, int keywordNum, ArticleMapper articleService){
        List<String> words = cut(content);
        words.addAll(cut(title));
        int total = words.size();//总词数
        HashMap<String,Double> tfidf = new HashMap<>();

        // 首先计算词频 TF
        for(String word:words){
            if(tfidf.containsKey(word)){
                tfidf.put(word,tfidf.get(word)+1);
            }
            else tfidf.put(word,1.0);
        }
        words.clear();// 不用的马上清空释放内存
        for(Map.Entry<String,Double> entry:tfidf.entrySet()){
            entry.setValue(entry.getValue() / total );
        }

        // 然后计算逆文档频率
        double articleTotal = articleService.countAll();//总文档个数
        for(Map.Entry<String,Double> entry: tfidf.entrySet()){
            int num = articleService.countLike("%"+entry.getKey()+"%") + 1;//包含该词的文档数
            entry.setValue(entry.getValue() * Math.log(articleTotal/num));
        }

        // 将计算后的保存为Keyword的列表
        List<Keyword> keywords = new ArrayList<>();
        for(Map.Entry<String,Double> entry: tfidf.entrySet()){
            keywords.add(new Keyword(entry.getKey(),entry.getValue()));
        }
        tfidf.clear();// 不用的清空释放内存
        if(keywords.size() > keywordNum){// 如果大于给定的个数，就排序,取前面重要的
            Collections.sort(keywords, new Comparator<Keyword>() {
                @Override
                public int compare(Keyword o1, Keyword o2) {
                    double diff = o1.getTfidf() - o2.getTfidf();
                    if(diff < 0)return 1;
                    else if(diff == 0)return 0;
                    return -1;
                }
            });
            keywords.subList(keywordNum,keywords.size()).clear();// 将超过的部分删掉
        }
        return keywords;
    }

    private static List<String> cut(String content){// 分词
        Document document = new Document(content);

        List<String> res = new ArrayList<>();
        for(Sentence sentence: document.sentences()){
            // 可作为关键词的词性： 名词，形容词, 动词
            // 名词：NN,NNS,NNP（专有名词）,NNPS, NT(时间名词),NP（名词短语），NR（固有名词） FW（外来词）
            // 形容词：JJ* , 副词： RB*
            // 动词： VB*
            int len = sentence.words().size();
            for(int i=0;i<len;++i){
                String tag = sentence.posTag(i);
                if(tag.charAt(0)=='N' || tag.contains("VB") || tag.contains("FW") ||
                        tag.contains("JJ") || tag.contains("RB")){
                    // 将词元添加进列表中
                    res.add(sentence.lemma(i));
                }
            }
        }

        return res;
    }
}
