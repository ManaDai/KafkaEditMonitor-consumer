package com.example.wikimedia_consumer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.wikimedia_consumer.domain.Keyword;

@DataMongoTest
public class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;

    private Keyword keyword1;
    private Keyword keyword2;
    private Keyword keyword3;

    @BeforeEach
    void setUo() {
        keyword1 = new Keyword("1", "keyword1");
        keyword2 = new Keyword("2", "keyword2");
        keyword3 = new Keyword("3", "keyword3");
        keywordRepository.save(keyword1);
        keywordRepository.save(keyword2);
        keywordRepository.save(keyword3);

    }

    @AfterEach
    void cleanUp() {
        keywordRepository.delete(keyword1);
        keywordRepository.delete(keyword2);
        keywordRepository.delete(keyword3);
    }

    @Test
    void testFindByKeyword() {

        Keyword foundKeyword = keywordRepository.findByKeyword("keyword1");
        assertNotNull(foundKeyword);
        assertEquals("1", foundKeyword.getKeywordId());
    }

    @Test
    void testFindByKeyword_NotFound() {

        Keyword nonexistentKeyword = keywordRepository.findByKeyword("nonexistentKeyword");
        assertNull(nonexistentKeyword);

    }

    @Test
    void testFindByKeywordId() {

        Keyword foundKeyword = keywordRepository.findByKeywordId("1");
        assertNotNull(foundKeyword);
        assertEquals("keyword1", foundKeyword.getKeyword());

    }

    @Test
    void testFindByKeywordId_NotFound() {

        Keyword nonexistentKeyword = keywordRepository.findByKeywordId("nonexistentKeywordId");
        assertNull(nonexistentKeyword);

    }


    @Test
    void testFindByKeywordIdIn() {
        List<String> keywordIds = List.of("1", "2");

        List<Keyword> foundKeywords = keywordRepository.findByKeywordIdIn(keywordIds);

        assertNotNull(foundKeywords);
        assertEquals(2, foundKeywords.size()); // 2つのIDが存在するので、サイズは2であるべき

        assertTrue(foundKeywords.stream().anyMatch(keyword -> "1".equals(keyword.getKeywordId())));
        assertTrue(foundKeywords.stream().anyMatch(keyword -> "2".equals(keyword.getKeywordId())));
    }

    @Test
    void testFindByKeywordIdIn_EmptyList() {
        List<String> keywordIds = List.of();

        List<Keyword> foundKeywords = keywordRepository.findByKeywordIdIn(keywordIds);

        assertNotNull(foundKeywords);
        assertTrue(foundKeywords.isEmpty()); // 空のリストを検索するので、結果も空であるべき
    }

}
