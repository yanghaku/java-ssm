package edu.study.util.crawler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

// 使用 WebMagic 抓取

@Component
public class VideoCrawler {

    @Autowired
    VideoMysqlPipeline videoMysqlPipeline;

    public void run(){
        Spider.create(new VideoPageProcessor())
                .addUrl("https://www.51voa.com/Learning_English_Videos_1.html")
                .addPipeline(videoMysqlPipeline)
                .run();
    }
}
