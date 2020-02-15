package edu.study.model;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {

    private static final long serialVersionUID = 1L;// 支持序列化

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.ARTICLE_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private Integer articleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.USERNAME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.CATEGORY_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private Integer categoryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.TITLE
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.CREATE_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.MODIFY_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private Date modifyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.CLICKS
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private Integer clicks;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column article.CONTENT
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.ARTICLE_ID
     *
     * @return the value of article.ARTICLE_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.ARTICLE_ID
     *
     * @param articleId the value for article.ARTICLE_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.USERNAME
     *
     * @return the value of article.USERNAME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.USERNAME
     *
     * @param username the value for article.USERNAME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.CATEGORY_ID
     *
     * @return the value of article.CATEGORY_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.CATEGORY_ID
     *
     * @param categoryId the value for article.CATEGORY_ID
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.TITLE
     *
     * @return the value of article.TITLE
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.TITLE
     *
     * @param title the value for article.TITLE
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.CREATE_TIME
     *
     * @return the value of article.CREATE_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.CREATE_TIME
     *
     * @param createTime the value for article.CREATE_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.MODIFY_TIME
     *
     * @return the value of article.MODIFY_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.MODIFY_TIME
     *
     * @param modifyTime the value for article.MODIFY_TIME
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.CLICKS
     *
     * @return the value of article.CLICKS
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public Integer getClicks() {
        return clicks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.CLICKS
     *
     * @param clicks the value for article.CLICKS
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column article.CONTENT
     *
     * @return the value of article.CONTENT
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column article.CONTENT
     *
     * @param content the value for article.CONTENT
     *
     * @mbg.generated Tue Feb 11 10:46:44 CST 2020
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}