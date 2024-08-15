package com.example.wikimedia_consumer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.wikimedia_consumer.domain.Edit;

import reactor.core.publisher.Flux;

@Repository
public interface EditRepository extends ReactiveMongoRepository<Edit, String> {

    Flux<Edit> findByKeywordId(String keywordId);

}
