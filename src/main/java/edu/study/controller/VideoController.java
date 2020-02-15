package edu.study.controller;

import edu.study.dao.VideoMapper;
import edu.study.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    VideoMapper videoService;

    // 获取视频数
    @RequestMapping("/videoCount")
    @ResponseBody
    Integer videoCount(){
        return videoService.countAll();
    }

    //获取视频列表（页数限制）
    @RequestMapping("/videoList")
    @ResponseBody
    List<Video> videoList(@RequestParam Integer start,@RequestParam Integer size){
        Map<String,Object> mp = new HashMap<>();
        if(start == null || size==null || start<0 || size<0)return null;
        mp.put("start",start);
        mp.put("size",size);
        return videoService.selectByPageLimit(mp);
    }

    // 获取单个视频信息
    @RequestMapping("/getSingleVideo")
    @ResponseBody
    Video getSingleVideo(@RequestParam Integer videoId){
        return videoService.selectByPrimaryKey(videoId);
    }
}

