package edu.study.util.timer;

import edu.study.util.crawler.ArticleCrawler;
import edu.study.util.crawler.VideoCrawler;
import edu.study.util.recommend.ContentBasedRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TimerJob {
    /**
     *  定时任务的类
     */

    @Autowired
    ContentBasedRecommend contentBasedRecommend;// 基于内容的推荐系统

    public void recommendRefresh(){
        System.err.println("refresh Start");
        contentBasedRecommend.refresh();
        System.err.println("refresh End");
    }


    @Autowired
    VideoCrawler videoCrawler; // 视频的爬虫

    public void videoCrawl(){ //这个爬一次即可，爬完即可获得全部视频
        System.err.println();
        System.err.println("video crawler start");
        videoCrawler.run();
        System.err.println("video crawler end");
    }


    @Autowired
    ArticleCrawler articleCrawler; // 文章的爬虫

    public void articleCrawl(){
        System.err.println();
        System.err.println("article crawler start");
        articleCrawler.run();
        System.err.println("article crawler end");
    }

}
