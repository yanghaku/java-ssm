package edu.study.dao;

import edu.study.model.WordRecited;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WordRecitedMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word_recited
     *
     * @mbg.generated Tue Jan 21 10:57:41 CST 2020
     */
    int deleteByPrimaryKey(@Param("username") String username, @Param("wordName") String wordName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word_recited
     *
     * @mbg.generated Tue Jan 21 10:57:41 CST 2020
     */
    int insert(WordRecited record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word_recited
     *
     * @mbg.generated Tue Jan 21 10:57:41 CST 2020
     */
    WordRecited selectByPrimaryKey(@Param("username") String username, @Param("wordName") String wordName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word_recited
     *
     * @mbg.generated Tue Jan 21 10:57:41 CST 2020
     */
    List<WordRecited> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word_recited
     *
     * @mbg.generated Tue Jan 21 10:57:41 CST 2020
     */
    int updateByPrimaryKey(WordRecited record);
}