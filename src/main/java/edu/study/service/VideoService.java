package edu.study.service;


import edu.study.model.Video;

import java.util.List;
import java.util.Map;

public interface VideoService {

    Integer countAll();

    List<Video> selectByPageLimit(Map<String,Object> mp);

    Video selectByPrimaryKey(Integer videoId);

    Integer videoInsertAndGetId(Video video);
}
