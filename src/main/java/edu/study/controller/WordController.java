package edu.study.controller;

import edu.study.dao.WordMapper;
import edu.study.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("word")
public class WordController {

    @Autowired
    WordMapper wordService;

    @RequestMapping(value = "getWords", method = RequestMethod.GET)
    @ResponseBody
    List<Word> getWords(HttpServletRequest request, @RequestParam String type,@RequestParam Integer num){
        // type: new collect review
        System.out.println("Type: "+type + " num: "+num);
        return  wordService.selectAll();
    }

}
