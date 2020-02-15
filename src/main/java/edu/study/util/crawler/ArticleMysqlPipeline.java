package edu.study.util.crawler;


import edu.study.dao.ArticleMapper;
import edu.study.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@Component
public class ArticleMysqlPipeline implements Pipeline {
    @Autowired
    ArticleMapper articleService;

    private static final Random random = new Random();

    @Override
    // 实现处理函数
    public void process(ResultItems resultItems, Task task) {
        Map<String,Object> mp=resultItems.getAll();
        Article article = new Article();
        article.setTitle((String)mp.get("title"));
        article.setContent((String)mp.get("content"));
        if(article.getTitle()==null || article.getContent()==null)return;
        if(article.getTitle().length()==0 || article.getContent().length()==0)return;

        article.setModifyTime(new Date());
        article.setCreateTime(new Date());
        article.setClicks(Math.abs(random.nextInt()));
        article.setCategoryId(1);// 新闻
        article.setUsername("123");//默认

        articleService.insertAndGetId(article);
    }
}
