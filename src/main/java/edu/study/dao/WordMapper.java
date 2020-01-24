package edu.study.dao;

import edu.study.model.Word;
import java.util.List;

public interface WordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word
     *
     * @mbg.generated Tue Jan 21 10:57:40 CST 2020
     */
    int deleteByPrimaryKey(String wordName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word
     *
     * @mbg.generated Tue Jan 21 10:57:40 CST 2020
     */
    int insert(Word record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word
     *
     * @mbg.generated Tue Jan 21 10:57:40 CST 2020
     */
    Word selectByPrimaryKey(String wordName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word
     *
     * @mbg.generated Tue Jan 21 10:57:40 CST 2020
     */
    List<Word> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table word
     *
     * @mbg.generated Tue Jan 21 10:57:40 CST 2020
     */
    int updateByPrimaryKey(Word record);
}