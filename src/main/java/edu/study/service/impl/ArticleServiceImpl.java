package edu.study.service.impl;


import edu.study.dao.*;
import edu.study.model.*;
import edu.study.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("ArticleService")
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commmentMapper;

    @Autowired
    ArticleAgreeMapper articleAgreeMapper;

    @Autowired
    ArticleCollectionMapper articleCollectionMapper;

    @Autowired
    ArticleRecommendMapper articleRecommendMapper;

    @Autowired
    ArticleKeywordMapper articleKeywordMapper;

    @Override
    public List<ArticleCategory> categorySelectAll() {
        return articleCategoryMapper.selectAll();
    }

    @Override
    public Integer articleCountByCategoryId(Integer categoryId) {
        return articleMapper.articleCountByCategoryId(categoryId);
    }

    @Override
    public Integer articlecountByAuthor(String username) {
        return articleMapper.countByAuthor(username);
    }

    @Override
    public Integer articleCountByCollect(String username) {
        return articleMapper.countByCollect(username);
    }

    @Override
    public Integer articleCountByCategoryIdCollect(Integer categoryId, String username) {
        return articleMapper.articleCountByCategoryIdCollect(categoryId,username);
    }

    @Override
    public Integer articleCountByCategoryIdUsername(Integer categoryId, String username) {
        return articleMapper.articleCountByCategoryIdUsername(categoryId,username);
    }

    @Override
    public List<Article> articleSelectByPageLimit(Map<String, Object> mp) {
        return articleMapper.selectByPageLimit(mp);
    }

    @Override
    public List<Article> articleSelectByPageLimitUsername(Map<String, Object> mp) {
        return articleMapper.selectByPageLimitUsername(mp);
    }

    @Override
    public List<Article> articleSelectByPageLimitCollect(Map<String, Object> mp) {
        return articleMapper.selectByPageLimitCollect(mp);
    }

    @Override
    public void updateArticleClicks(Integer articleId) {
        articleMapper.updateArticleClicks(articleId);
    }

    @Override
    public Article articleSelectByPrimaryKey(Integer articleId) {
        return articleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public Integer articleInsertAndGetId(Article article) {
        return articleMapper.insertAndGetId(article);
    }

    @Override
    public Integer articleUpdateByPrimaryKey(Article article) {
        return articleMapper.updateByPrimaryKey(article);
    }

    @Override
    public List<Article> articleSelectByclicks(Integer num) {
        return articleMapper.selectByclicks(num);
    }

    @Override
    public List<Comment> commentSelectByArticleId(Integer articleId) {
        return commmentMapper.selectByArticleId(articleId);
    }

    @Override
    public Integer commentInsertAndGetId(Comment comment) {
        return commmentMapper.insertAndGetId(comment);
    }

    @Override
    public Integer commentCountByArticleId(Integer articleId) {
        return commmentMapper.countByArticleId(articleId);
    }

    @Override
    public Integer collectCountByArticleId(Integer articleId) {
        return articleCollectionMapper.countByArticleId(articleId);
    }

    @Override
    public ArticleCollection collectSelectByPrimaryKey(String username, Integer articleId) {
        return articleCollectionMapper.selectByPrimaryKey(username,articleId);
    }

    @Override
    public Integer collectInsert(ArticleCollection articleCollection) {
        return articleCollectionMapper.insert(articleCollection);
    }

    @Override
    public Integer agreeCountByArticleId(Integer articleId) {
        return articleAgreeMapper.countByArticleId(articleId);
    }

    @Override
    public ArticleAgree selectByPrimaryKey(String username, Integer articleId) {
        return articleAgreeMapper.selectByPrimaryKey(username,articleId);
    }

    @Override
    public Integer agreeInsert(ArticleAgree articleAgree) {
        return articleAgreeMapper.insert(articleAgree);
    }

    @Override
    public List<Integer> recommendSelectByUsername(String username) {
        return articleRecommendMapper.selectByUsername(username);
    }

    @Override
    public Integer articleCountAll() {
        return articleMapper.countAll();
    }

    @Override
    public Integer articleCountLike(String str) {
        return articleMapper.countLike(str);
    }

    @Override
    public List<ArticleAgree> agreeSelectByTime(Date time) {
        return articleAgreeMapper.selectByTime(time);
    }

    @Override
    public List<ArticleCollection> collectSelectByTime(Date time) {
        return articleCollectionMapper.selectByTime(time);
    }

    @Override
    public List<Keyword> keywordSelectByArticleId(Integer articleId) {
        return articleKeywordMapper.selectByArticleId(articleId);
    }

    @Override
    public void keywordReplaceInto(Integer articleId, List<Keyword> list) {
        articleKeywordMapper.replaceInto(articleId,list);
    }

    @Override
    public List<Article> articleSelectByModifyTime(Date time) {
        return articleMapper.selectByModifyTime(time);
    }

    @Override
    public void recommendDeleteByUsername(String username) {
        articleRecommendMapper.deleteByUsername(username);
    }

    @Override
    public void recommendInsertList(List<ArticleRecommend> keywordList) {
        articleRecommendMapper.insertList(keywordList);
    }

    @Override
    public List<ArticleKeyword> articleKeywordSelectAll() {
        return articleKeywordMapper.selectAll();
    }
}
