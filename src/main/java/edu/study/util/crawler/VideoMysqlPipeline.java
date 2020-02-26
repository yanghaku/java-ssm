package edu.study.util.crawler;

import edu.study.model.Video;
import edu.study.service.VideoService;
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
    VideoService videoService;

    @Override
    // 实现处理函数
    public void process(ResultItems resultItems, Task task) {
        Map<String,Object> mp=resultItems.getAll();
        Video video = new Video();
        video.setCreateTime(new Date());
        video.setDescription((String)mp.get("description"));
        video.setVideoName((String) mp.get("name"));
        video.setVideoAddress((String) mp.get("url"));
        video.setLabel("VOA");
        videoService.videoInsertAndGetId(video);
    }

}
