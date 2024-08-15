package com.example.wikimedia_consumer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.wikimedia_consumer.domain.Keyword;

public interface KeywordRepository extends MongoRepository<Keyword, String> {

    Keyword findByKeyword(String keywordInput);

    @Query("{ 'keywordId': { $in: ?0 } }")
    List<Keyword> findByKeywordIdIn(List<String> keywordIds);


    Keyword findByKeywordId(String keywordId);

    
}
