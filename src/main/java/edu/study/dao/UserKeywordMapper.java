package edu.study.dao;

import edu.study.model.Keyword;
import edu.study.model.UserKeyword;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserKeywordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_keyword
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int deleteByPrimaryKey(@Param("username") String username, @Param("keyword") String keyword);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_keyword
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int insert(UserKeyword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_keyword
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    UserKeyword selectByPrimaryKey(@Param("username") String username, @Param("keyword") String keyword);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_keyword
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    List<UserKeyword> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_keyword
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int updateByPrimaryKey(UserKeyword record);

    // 选择某个用户的所有关键词
    List<Keyword> selectByUsername(String username);

    // 批量操作，对于每个 (username,keyword) 如果不存在就插入，如果存在就更新
    void replaceInto(@Param("username")String username,@Param("keywordList")List<Keyword> keywordList);

    // 将所有的tfidf值乘一个系数
    void mulAll(Double x);
}