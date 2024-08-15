package com.example.wikimedia_consumer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.domain.UserKeyword;
import com.example.wikimedia_consumer.repository.KeywordRepository;
import com.example.wikimedia_consumer.repository.UserKeywordRepository;

@Service
public class UserKeywordService {

    @Autowired
    private UserKeywordRepository userKeywordRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    public UserKeyword createUserKeyword(String appUserId, String keywordId) {

        UserKeyword userKeyword = new UserKeyword();
        userKeyword.setAppUserId(appUserId);
        userKeyword.setKeywordId(keywordId);
        UserKeyword storedUserKeyword = userKeywordRepository.save(userKeyword);

        return storedUserKeyword;

    }

    public List<UserKeyword> findByAppUserId(String appUserId) {

        return userKeywordRepository.findByAppUserId(appUserId);

    }

    public List<Keyword> findKeywordsByUserKeywords(List<UserKeyword> userKeywordList) {
        List<String> keywordIds = userKeywordList.stream()
                .map(UserKeyword::getKeywordId)
                .collect(Collectors.toList());
        return keywordRepository.findByKeywordIdIn(keywordIds);
    }

    public void deleteByKeywordId(String keywordId) {
        userKeywordRepository.deleteByKeywordId(keywordId);
    }

  
    public List<String> findKeywordIdsByUserId(String appUserId) {
        return userKeywordRepository.findByAppUserId(appUserId).stream()
            .map(userKeyword -> userKeyword.getKeywordId())  
            .collect(Collectors.toList()); 
    }
    

}
