package edu.study.service.impl;


import edu.study.dao.VideoMapper;
import edu.study.model.Video;
import edu.study.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("VideoService")
@Transactional
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoMapper videoService;


    @Override
    public Integer countAll() {
        return videoService.countAll();
    }

    @Override
    public List<Video> selectByPageLimit(Map<String, Object> mp) {
        return videoService.selectByPageLimit(mp);
    }

    @Override
    public Video selectByPrimaryKey(Integer videoId) {
        return videoService.selectByPrimaryKey(videoId);
    }

    @Override
    public Integer videoInsertAndGetId(Video video) {
        return videoService.insertAndGetId(video);
    }
}
