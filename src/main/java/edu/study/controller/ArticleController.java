package edu.study.controller;

import edu.study.dao.ArticleCategoryMapper;
import edu.study.dao.ArticleCollectionMapper;
import edu.study.dao.ArticleMapper;
import edu.study.dao.CommentMapper;
import edu.study.model.Article;
import edu.study.model.ArticleCategory;
import edu.study.model.ArticleCollection;
import edu.study.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "article")
public class ArticleController {

    @Autowired
    ArticleCategoryMapper articleCategoryService;

    @Autowired
    ArticleMapper articleService;

    @Autowired
    CommentMapper commmentService;

    @Autowired
    ArticleCollectionMapper articleCollectionService;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseBody
    public List<ArticleCategory> getCategory(){
        return articleCategoryService.selectAll();
    }

    @RequestMapping(value = "/articleList", method = RequestMethod.GET)
    @ResponseBody
    public List<Article> getArticleListByPageLimit(@RequestParam Integer categoryId, @RequestParam Integer start,
                                    @RequestParam Integer size){
        HashMap<String,Object> mp=new HashMap<>();
        mp.put("categoryId",categoryId);
        mp.put("start",start);
        mp.put("size",size);
        return articleService.selectByPageLimit(mp);
    }

    @RequestMapping(value = "/getSingleArticle", method = RequestMethod.GET)
    @ResponseBody
    public Article getArticle(HttpServletRequest request, @RequestParam Integer articleId){
        if(request.getSession().getAttribute("current_user") == null)return null;
        // 点击量加一
        articleService.updateArticleClicks(articleId);
        return articleService.selectByPrimaryKey(articleId);
    }

    @RequestMapping(value = "/insertArticle", method = RequestMethod.POST)
    @ResponseBody
    public Integer insertArticle(HttpServletRequest request, @RequestBody Article article){
        article.setUsername((String)request.getSession().getAttribute("current_user"));
        if(article.getUsername() == null) return -1;
        article.setAgrees(0); article.setClicks(0);
        article.setCreateTime(new Date());
        article.setModifyTime(new Date());
        if(articleService.insertAndGetId(article) == 1){
            return article.getArticleId();
        }
        return -1;
    }

    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    @ResponseBody
    public Integer updateArticle(HttpServletRequest request, @RequestBody Article article){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || !username.equals(article.getUsername())) return -1;
        article.setModifyTime(new Date());
        if(articleService.updateByPrimaryKey(article) == 1){
            return article.getArticleId();
        }
        return -1;
    }

    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    @ResponseBody
    public List<Comment> getComment(HttpServletRequest request,@RequestParam Integer articleId){
        if(request.getSession().getAttribute("current_user") == null)return null;
        return commmentService.selectByArticleId(articleId);
    }

    @RequestMapping(value = "/insertComment", method = RequestMethod.POST)
    @ResponseBody
    public Comment insertComment(HttpServletRequest request,@RequestBody Comment comment){
        comment.setUsername((String)request.getSession().getAttribute("current_user"));
        if(comment.getUsername() == null)return null;
        comment.setCreateTime(new Date());
        if( commmentService.insertAndGetId(comment) == 1) return comment;
        return null;
    }

    // 获取某一分类的文章数
    @RequestMapping(value = "/articleCount", method = RequestMethod.GET)
    @ResponseBody
    public Integer articleCountByCategoryId(@RequestParam Integer categoryId){
        return articleService.articleCountByCategoryId(categoryId);
    }

    @RequestMapping(value = "/articleCollectCount",method = RequestMethod.GET)
    @ResponseBody
    public Integer articleCountByCollect(@RequestParam Integer articleId){
        return articleCollectionService.countByArticleId(articleId);
    }

    @RequestMapping(value = "/commentCount", method = RequestMethod.GET)
    @ResponseBody
    public Integer commentCount(@RequestParam Integer articleId){
        return commmentService.countByArticleId(articleId);
    }

    @RequestMapping(value = "/articleAgree", method = RequestMethod.GET)
    @ResponseBody
    public Integer articleAgree(HttpServletRequest request, @RequestParam Integer articleId){
        if(request.getSession().getAttribute("current_user") == null)return null;
        return articleService.updateArticleAgrees(articleId);
    }

    @RequestMapping(value = "/getArticleCollect", method = RequestMethod.GET)
    @ResponseBody
    public Integer getArticleCollect(HttpServletRequest request, @RequestParam Integer articleId){//判断文章是否被收藏
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || articleCollectionService.selectByPrimaryKey(username,articleId) == null )
            return 0;
        return 1;
    }

    @RequestMapping(value = "/insertArticleCollect", method = RequestMethod.GET)
    @ResponseBody
    public Integer insertArticleCollect(HttpServletRequest request, @RequestParam Integer articleId){//文章收藏
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null) return 0;
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setUsername(username);
        articleCollection.setArticleId(articleId);
        articleCollection.setTime(new Date());
        return articleCollectionService.insert(articleCollection);
    }
}
