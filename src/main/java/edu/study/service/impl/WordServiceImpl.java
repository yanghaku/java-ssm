package edu.study.service.impl;

import edu.study.dao.WordCollectionMapper;
import edu.study.dao.WordMapper;
import edu.study.dao.WordRecitedMapper;
import edu.study.model.Word;
import edu.study.model.WordCollection;
import edu.study.model.WordRecited;
import edu.study.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("WordService")
@Transactional
public class WordServiceImpl implements WordService {

    @Autowired
    WordMapper wordMapper;

    @Autowired
    WordCollectionMapper wordCollectionMapper;

    @Autowired
    WordRecitedMapper wordRecitedMapper;

    @Override
    public List<Word> selectNew(String username, Integer num) {
        return wordMapper.selectNew(username,num);
    }

    @Override
    public List<Word> selectCollect(String username, Integer num) {
        return wordMapper.selectCollect(username,num);
    }

    @Override
    public List<Word> selectReview(String username, Integer num) {
        return wordMapper.selectReview(username,num);
    }

    @Override
    public WordCollection collectSelectByPrimaryKey(String username, String wordName) {
        return wordCollectionMapper.selectByPrimaryKey(username,wordName);
    }

    @Override
    public Integer reciteCountByPrimaryKey(String username, String wordName) {
        return wordRecitedMapper.countByPrimaryKey(username,wordName);
    }

    @Override
    public Integer collectInsert(WordCollection wordCollection) {
        return wordCollectionMapper.insert(wordCollection);
    }

    @Override
    public Integer reciteReplace(WordRecited wordRecited) {
        return wordRecitedMapper.insert(wordRecited);
    }

    @Override
    public Integer reciteCountByUsername(String username) {
        return wordRecitedMapper.countByUsername(username);
    }

    @Override
    public Integer collectCountByUsername(String username) {
        return wordCollectionMapper.countByUsername(username);
    }
}
