package com.example.wikimedia_consumer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.repository.KeywordRepository;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    public boolean isAlreadyRegisteredKeyword(String keywordInput) {
        Keyword keyword = keywordRepository.findByKeyword(keywordInput);
        if (keyword != null) {
            return true;
        } else {
            return false;
        }
    }

    public Keyword saveKeyword(Keyword keyword) {
        Keyword registeredKeyword = keywordRepository.save(keyword);
        return registeredKeyword;
    }

    public Keyword findById(String keywordId) {
        return keywordRepository.findByKeywordId(keywordId);
    }

    public void deleteById(String keywordId) {
        keywordRepository.deleteById(keywordId);

    }

    // キーワードIDリストを使ってキーワードエンティティのリストを取得
    public List<String> findKeywordsByKeywordIds(List<String> keywordIds) {
        List<Keyword> keywords = keywordRepository.findByKeywordIdIn(keywordIds);
        return keywords.stream()
                .map(Keyword::getKeyword)
                .collect(Collectors.toList());
    }

}
