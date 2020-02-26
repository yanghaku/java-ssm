package edu.study.controller;

import edu.study.model.Word;
import edu.study.model.WordCollection;
import edu.study.model.WordRecited;
import edu.study.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("word")
public class WordController {

    @Autowired
    WordService wordService;

    @RequestMapping(value = "getWords", method = RequestMethod.GET)
    @ResponseBody
    List<Word> getWords(HttpServletRequest request, @RequestParam String type,@RequestParam Integer num){
        String username = (String)request.getSession().getAttribute("current_user");
        // type: new collect review
        // new: 既没收藏又没记过的单词
        // collect： 收藏的单词
        // review: 已记住的单词
        if(type.equals("new")){
            return wordService.selectNew(username,num);
        }
        else if(type.equals("collect")){
            return wordService.selectCollect(username,num);
        }
        return wordService.selectReview(username,num);// review
    }


    // 判断是否被收藏
    @RequestMapping(value = "judgeCollected",method = RequestMethod.GET)
    @ResponseBody
    Integer judgeCollected(HttpServletRequest request,@RequestParam String wordName){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || wordName==null)return null;
        if(wordService.collectSelectByPrimaryKey(username,wordName) != null)return 1;
        return 0;
    }

    //判断是否被记住
    @RequestMapping(value = "judgeRecited", method = RequestMethod.GET)
    @ResponseBody
    Integer judgeRecited(HttpServletRequest request,@RequestParam String wordName){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || wordName==null)return null;
        return wordService.reciteCountByPrimaryKey(username,wordName);
    }

    // 新增收藏单词
    @RequestMapping(value = "collectWord", method = RequestMethod.GET)
    @ResponseBody
    Integer collectWord(HttpServletRequest request,@RequestParam String wordName){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || wordName==null)return null;
        WordCollection wordCollection = new WordCollection();
        wordCollection.setTime(new Date());
        wordCollection.setUsername(username);
        wordCollection.setWordName(wordName);
        return wordService.collectInsert(wordCollection);
    }

    // 新增记住单词
    @RequestMapping(value = "reciteWord",method = RequestMethod.GET)
    @ResponseBody
    Integer reciteWord(HttpServletRequest request,@RequestParam String wordName){
        String username = (String)request.getSession().getAttribute("current_user");
        if(username == null || wordName==null)return null;
        WordRecited wordRecited = new WordRecited();
        wordRecited.setRepeats(0);
        wordRecited.setLastRecite(new Date());
        wordRecited.setUsername(username);
        wordRecited.setWordName(wordName);
        return wordService.reciteReplace(wordRecited);
    }

    // 查询用户记住的单词量
    @RequestMapping(value = "countRecite",method = RequestMethod.GET)
    @ResponseBody
    Integer countRecite(@RequestParam String username){
        if(username == null)return 0;
        return wordService.reciteCountByUsername(username);
    }

    // 查询用户收藏的单词量
    @RequestMapping(value = "countCollect", method = RequestMethod.GET)
    @ResponseBody
    Integer countCollect(@RequestParam String username){
        if(username == null)return 0;
        return wordService.collectCountByUsername(username);
    }
}
