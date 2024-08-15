package com.example.wikimedia_consumer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.wikimedia_consumer.domain.Edit;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
public class EditRepositoryTest {

    @Autowired
    private EditRepository editRepository;

    private Edit edit;

    @BeforeEach
    void setUp() {
        edit = new Edit();
        edit.setKeywordId("keywordId");
        edit.setEditId("editId");
        edit.setPageTitle("title");
        edit.setUser("user");
        edit.setComment("comment");
        edit.setTimestamp("time");
        edit.setTitleUrl("url");
        editRepository.save(edit).block(); // 非同期操作をブロックして待機
    }

    @AfterEach
    void cleanUp() {
        editRepository.delete(edit).block(); 
    }


    @Test
    void testFindByKeywordId() {
        Flux<Edit> edits = editRepository.findByKeywordId("keywordId");

        StepVerifier.create(edits)
                .expectNextMatches(foundEdit -> {
                    assertEquals("keywordId", foundEdit.getKeywordId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testFindByKeywordId_NotFound() {
        Flux<Edit> edits = editRepository.findByKeywordId("nonExistingKeywordId");

        StepVerifier.create(edits)
                .expectNextCount(0) 
                .verifyComplete();
    }


}
