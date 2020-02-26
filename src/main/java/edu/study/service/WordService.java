package edu.study.service;

import edu.study.model.Word;
import edu.study.model.WordCollection;
import edu.study.model.WordRecited;

import java.util.List;

public interface WordService {

    List<Word> selectNew(String username,Integer num);

    List<Word> selectCollect(String username, Integer num);

    List<Word> selectReview(String username,Integer num);

    WordCollection collectSelectByPrimaryKey(String username,String wordName);

    Integer reciteCountByPrimaryKey(String username,String wordName);

    Integer collectInsert(WordCollection wordCollection);

    Integer reciteReplace(WordRecited wordRecited);

    Integer reciteCountByUsername(String username);

    Integer collectCountByUsername(String username);
}
