package edu.study.crawler;

import edu.study.dao.VideoMapper;
import edu.study.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.Map;

@Component
public class VideoMysqlPipeline implements Pipeline {

    @Autowired
    VideoMapper videoService;

    public VideoMysqlPipeline(){}

    @Override
    // 实现处理函数
    public void process(ResultItems resultItems, Task task) {
        Map<String,Object> mp=resultItems.getAll();
        Video video = new Video();
        video.setCreateTime(new Date());
        video.setDescription("description");
        video.setVideoName((String) mp.get("name"));
        video.setVideoAddress((String) mp.get("url"));
        System.err.println(video);
        videoService.insert(video);
        System.err.println("id = " + video.getVideoId());
    }

//    public static void main(String[] args){
//        Spider spider=Spider.create(new VideoPageProcessor());
//        spider.addUrl("https://www.51voa.com/VOA_Videos/january-22-2020-83737.html");
//        spider.addPipeline(new VideoMysqlPipeline());
//        spider.run();//启动爬虫
//    }

}
