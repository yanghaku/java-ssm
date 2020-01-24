package edu.study.controller;

import edu.study.crawler.VideoMysqlPipeline;
import edu.study.crawler.VideoPageProcessor;
import edu.study.dao.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Spider;


@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    VideoMapper videoMapper;

    @RequestMapping("spider")
    @ResponseBody
    String spider(){
        Spider spider=Spider.create(new VideoPageProcessor());
        spider.addUrl("https://www.51voa.com/VOA_Videos/january-22-2020-83737.html");
        spider.addPipeline(new VideoMysqlPipeline());
        spider.run();//启动爬虫
        return "OK";
    }

}

