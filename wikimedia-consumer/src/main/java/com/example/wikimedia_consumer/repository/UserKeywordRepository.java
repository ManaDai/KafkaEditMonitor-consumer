package com.example.wikimedia_consumer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.wikimedia_consumer.domain.UserKeyword;

@Repository
public interface UserKeywordRepository extends MongoRepository<UserKeyword, String> {

    List<UserKeyword> findByAppUserId(String appUserId);

    void deleteByKeywordId(String keywordId);

}
