package edu.study.service.impl;


import edu.study.dao.UserKeywordMapper;
import edu.study.dao.UserMapper;
import edu.study.model.Keyword;
import edu.study.model.User;
import edu.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserKeywordMapper userKeywordMapper;


    @Override
    public User userSelectByPrimaryKey(String username) {
        return userMapper.selectByPrimaryKey(username);
    }

    @Override
    public void userInsert(User user) {
        userMapper.insert(user);
    }

    @Override
    public Integer userUpdateByPrimaryKey(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public List<Keyword> keywordSelectByUsername(String username) {
        return userKeywordMapper.selectByUsername(username);
    }

    @Override
    public void keywordReplaceInto(String username, List<Keyword> list) {
        userKeywordMapper.replaceInto(username,list);
    }

    @Override
    public void keywordMulAll(double dec) {
        userKeywordMapper.mulAll(dec);
    }
}
