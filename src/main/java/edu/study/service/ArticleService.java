package edu.study.service;

import edu.study.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ArticleService {

    // 文章分类相关
    List<ArticleCategory> categorySelectAll();

    Integer articleCountByCategoryId(Integer categoryId);


    // 文章相关

    Integer articlecountByAuthor(String username);

    Integer articleCountByCollect(String username);

    Integer articleCountByCategoryIdCollect(Integer categoryId, String username);

    Integer articleCountByCategoryIdUsername(Integer categoryId, String username);

    List<Article> articleSelectByPageLimit(Map<String, Object> mp);

    List<Article> articleSelectByPageLimitUsername(Map<String, Object> mp);

    List<Article> articleSelectByPageLimitCollect(Map<String, Object> mp);

    void updateArticleClicks(Integer articleId);

    Article articleSelectByPrimaryKey(Integer articleId);

    Integer articleInsertAndGetId(Article article);

    Integer articleUpdateByPrimaryKey(Article article);

    List<Article> articleSelectByclicks(Integer num);

    // 文章评论相关

    List<Comment> commentSelectByArticleId(Integer articleId);

    Integer commentInsertAndGetId(Comment comment);

    Integer commentCountByArticleId(Integer articleId);


    // 文章收藏相关
    Integer collectCountByArticleId(Integer articleId);

    ArticleCollection collectSelectByPrimaryKey(String username, Integer articleId);

    Integer collectInsert(ArticleCollection articleCollection);


    // 文章点赞相关

    Integer agreeCountByArticleId(Integer articleId);

    ArticleAgree selectByPrimaryKey(String username, Integer articleId);

    Integer agreeInsert(ArticleAgree articleAgree);


    // 文章推荐相关
    List<Integer> recommendSelectByUsername(String username);

    Integer articleCountAll();

    Integer articleCountLike(String str);

    List<ArticleAgree> agreeSelectByTime(Date time);

    List<ArticleCollection> collectSelectByTime(Date time);

    List<Keyword> keywordSelectByArticleId(Integer articleId);

    void keywordReplaceInto(Integer articleId,List<Keyword> list);

    List<Article> articleSelectByModifyTime(Date time);

    void recommendDeleteByUsername(String username);

    void recommendInsertList(List<ArticleRecommend> keywordList);

    List<ArticleKeyword> articleKeywordSelectAll();
}
