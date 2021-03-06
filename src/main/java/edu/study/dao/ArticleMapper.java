package edu.study.dao;

import edu.study.model.Article;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table article
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int deleteByPrimaryKey(Integer articleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table article
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int insert(Article record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table article
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    Article selectByPrimaryKey(Integer articleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table article
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    List<Article> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table article
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    int updateByPrimaryKey(Article record);

    int insertAndGetId(Article article);// 插入新的article并且获得对应的artcileID


    int articleCountByCategoryId(Integer categoryId);// 获得某个文章类别的文章数目

    int articleCountByCategoryIdUsername(@Param("categoryId") Integer categoryId,@Param("username") String username);
    // 获取某个文章类别，作者是username的文章数目

    int articleCountByCategoryIdCollect(@Param("categoryId") Integer categoryId,@Param("username") String username);
    // 获取某个文章分类，username收藏的文章数目


    List<Article> selectByPageLimit(Map<String,Object> mp); // 获取某个分类的文章列表

    List<Article> selectByPageLimitUsername(Map<String,Object> mp); // 获取某个文章分类的作者是username的文章列表

    List<Article> selectByPageLimitCollect(Map<String,Object> mp);// 获取某个分类的username的收藏列表


    int updateArticleClicks(Integer articleId);// 文章点击数加一

    List<Article> selectByModifyTime(Date date);// 获取修改时间从date至今的所有的article

    int countAll();//返回所有文章的总数

    int countLike(String str);//like模糊查询，返回包含这个子串的数目

    List<Article> selectByclicks(Integer num);// 获取点击量最多的num篇文章

    int countByAuthor(String username);// 获取用户发表的文章总数

    int countByCollect(String username);// 获取用户收藏的文章总数
}