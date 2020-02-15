package edu.study.util.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;


// 使用webmagic 抓取


@Component
public class ArticleCrawler {
    @Autowired
    ArticleMysqlPipeline articleMysqlPipeline;

    public void run(){
        Spider.create(new ArticlePageProcessor())
                .addUrl("http://www.hxen.com/englishnews/index.html")
                .addPipeline(articleMysqlPipeline)
                .run();
    }

}
