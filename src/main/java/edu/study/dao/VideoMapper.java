package edu.study.dao;

import edu.study.model.Video;
import java.util.List;
import java.util.Map;

public interface VideoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table video
     *
     * @mbg.generated Fri Jan 17 14:50:36 CST 2020
     */
    int deleteByPrimaryKey(Integer videoId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table video
     *
     * @mbg.generated Fri Jan 17 14:50:36 CST 2020
     */
    int insert(Video record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table video
     *
     * @mbg.generated Fri Jan 17 14:50:36 CST 2020
     */
    Video selectByPrimaryKey(Integer videoId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table video
     *
     * @mbg.generated Fri Jan 17 14:50:36 CST 2020
     */
    List<Video> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table video
     *
     * @mbg.generated Fri Jan 17 14:50:36 CST 2020
     */
    int updateByPrimaryKey(Video record);

    int insertAndGetId(Video record);

    int countAll();

    List<Video> selectByPageLimit(Map<String,Object> mp);
}