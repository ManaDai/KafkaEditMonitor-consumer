package com.example.wikimedia_consumer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.wikimedia_consumer.domain.UserKeyword;

@DataMongoTest
public class UserKeywordRepositoryTest {

    @Autowired
    private UserKeywordRepository userKeywordRepository;

    private UserKeyword userKeyword;

    @BeforeEach
    void setUp() {
        userKeyword = new UserKeyword();
        userKeyword.setAppUserId("appUserId");
        userKeyword.setKeywordId("keywordId");
        userKeywordRepository.save(userKeyword);
    }

    @AfterEach
    void cleanUp() {
        userKeywordRepository.delete(userKeyword);
    }

    @Test
    void testDeleteByKeywordId() {
        userKeywordRepository.deleteByKeywordId("keywordId");
        List<UserKeyword> userKeywords = userKeywordRepository.findByAppUserId("appUserId");
        assertTrue(userKeywords.isEmpty());

    }

    @Test
    void testDeleteByKeywordId_NotFound() {
        userKeywordRepository.deleteByKeywordId("nonExistingKeywordId");
        List<UserKeyword> userKeywords = userKeywordRepository.findByAppUserId("appUserId");
        assertEquals(1, userKeywords.size()); // データはまだ存在することを確認
    }

    @Test
    void testFindByAppUserId() {

        List<UserKeyword> foundUserKeywords = userKeywordRepository.findByAppUserId("appUserId");
        assertNotNull(foundUserKeywords);
        assertEquals(1, foundUserKeywords.size());
        assertEquals("keywordId", foundUserKeywords.get(0).getKeywordId());

    }

    @Test
    void testFindByAppUserId_NotFound() {
        List<UserKeyword> foundUserKeywords = userKeywordRepository.findByAppUserId("nonExistingAppUserId");
        assertTrue(foundUserKeywords.isEmpty());
    }

}
