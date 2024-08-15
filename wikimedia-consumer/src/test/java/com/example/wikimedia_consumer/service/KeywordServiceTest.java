package com.example.wikimedia_consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.repository.KeywordRepository;

@ExtendWith(MockitoExtension.class) // Mockitoの初期化を行う
public class KeywordServiceTest {

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordService keywordService;

    @Test
    void testDeleteById() {

        Keyword testKeyword = new Keyword();
        testKeyword.setKeywordId("123");
        testKeyword.setKeyword("test");

        keywordService.deleteById("123");

        verify(keywordRepository).deleteById("123");

    }

    @Test
    void testFindById() {
        Keyword testKeyword = new Keyword();
        testKeyword.setKeywordId("123");
        testKeyword.setKeyword("test");

        when(keywordRepository.findByKeywordId("123")).thenReturn(testKeyword);

        Keyword result = keywordService.findById("123");

        assertNotNull(result);
        assertEquals("123", result.getKeywordId());


    }

    @Test
    void testFindKeywordsByKeywordIds() {

        List<String> keywordIds = Arrays.asList("123", "456");

        List<Keyword> keywords = Arrays.asList(
            new Keyword("123", "keyword1"),
            new Keyword("456", "keyword2")
        );

        when(keywordRepository.findByKeywordIdIn(keywordIds)).thenReturn(keywords);

        List<String> result = keywordService.findKeywordsByKeywordIds(keywordIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("keyword1"));
        assertTrue(result.contains("keyword2"));
    }

    @Test
    void testIsAlreadyRegisteredKeyword() {
        Keyword testKeyword = new Keyword();
        testKeyword.setKeywordId("123");
        testKeyword.setKeyword("test");

        when(keywordRepository.findByKeyword("test")).thenReturn(testKeyword);
        boolean result = keywordService.isAlreadyRegisteredKeyword("test");
        assertTrue(result);

        when(keywordRepository.findByKeyword("nonexistentKeyword")).thenReturn(null);
        boolean resultForNonExistentKeyword = keywordService.isAlreadyRegisteredKeyword("nonexistentKeyword");
        assertFalse(resultForNonExistentKeyword);

    }

    @Test
    void testSaveKeyword() {
        Keyword testKeyword = new Keyword();
        testKeyword.setKeywordId("123");
        testKeyword.setKeyword("test");

        when(keywordRepository.save(testKeyword)).thenReturn(testKeyword);

        Keyword result = keywordService.saveKeyword(testKeyword);

        assertNotNull(result);
        assertEquals("test", result.getKeyword());


    }
}
