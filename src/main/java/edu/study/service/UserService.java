package edu.study.service;

import edu.study.model.Keyword;
import edu.study.model.User;

import java.util.List;

public interface UserService {

    User userSelectByPrimaryKey(String username);

    void userInsert(User user);

    Integer userUpdateByPrimaryKey(User user);


    // user keyword

    List<Keyword> keywordSelectByUsername(String username);

    void keywordReplaceInto(String username,List<Keyword> list);

    void keywordMulAll(double dec);
}
