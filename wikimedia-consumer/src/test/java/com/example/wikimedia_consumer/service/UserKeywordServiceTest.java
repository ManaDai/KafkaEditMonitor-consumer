package com.example.wikimedia_consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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
import com.example.wikimedia_consumer.domain.UserKeyword;
import com.example.wikimedia_consumer.repository.KeywordRepository;
import com.example.wikimedia_consumer.repository.UserKeywordRepository;

@ExtendWith(MockitoExtension.class)
public class UserKeywordServiceTest {

    @Mock
    private UserKeywordRepository userKeywordRepository;

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private UserKeywordService userKeywordService;

    @Test
    void testCreateUserKeyword() {

        UserKeyword userKeyword = new UserKeyword();
        userKeyword.setAppUserId("testAppUserId");
        userKeyword.setKeywordId("testKeywordId");

        when(userKeywordRepository.save(any(UserKeyword.class))).thenReturn(userKeyword);

        UserKeyword result = userKeywordService.createUserKeyword("testAppUserId", "testKeywordId");

        assertNotNull(result);
        assertEquals("testAppUserId", result.getAppUserId());
        assertEquals("testKeywordId", result.getKeywordId());
        verify(userKeywordRepository, times(1)).save(any(UserKeyword.class));

    }

    @Test
    void testDeleteByKeywordId() {

        String keywordId = "keywordId";

        doNothing().when(userKeywordRepository).deleteByKeywordId(keywordId);

        userKeywordService.deleteByKeywordId(keywordId);

        verify(userKeywordRepository, times(1)).deleteByKeywordId(keywordId);
    }

    @Test
    void testFindByAppUserId() {

        List<UserKeyword> userKeywords = Arrays.asList(
                new UserKeyword("userKeywordId1", "appUserId", "keywordId1"),
                new UserKeyword("userKeywordId2", "appUserId", "keywordId2")

        );

        when(userKeywordRepository.findByAppUserId("appUserId")).thenReturn(userKeywords);
        List<UserKeyword> result = userKeywordService.findByAppUserId("appUserId");

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(uk -> "keywordId1".equals(uk.getKeywordId())));
        assertTrue(result.stream().anyMatch(uk -> "keywordId2".equals(uk.getKeywordId())));

        verify(userKeywordRepository, times(1)).findByAppUserId("appUserId");

        assertEquals(userKeywords, result);
    }

    @Test
    void testFindKeywordIdsByUserId() {

        String appUserId = "user1";

        List<UserKeyword> userKeywords = Arrays.asList(
                new UserKeyword("userKeywordId1", appUserId, "keyword1"),
                new UserKeyword("userKeywordId2", appUserId, "keyword2"));

        when(userKeywordRepository.findByAppUserId(appUserId)).thenReturn(userKeywords);

        List<String> result = userKeywordService.findKeywordIdsByUserId(appUserId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("keyword1"));
        assertTrue(result.contains("keyword2"));
        verify(userKeywordRepository, times(1)).findByAppUserId(appUserId);
    }

    @Test
    void testFindKeywordsByUserKeywords() {
        String appUserId = "user1";

         List<UserKeyword> userKeywords = Arrays.asList(
            new UserKeyword("userKeywordId1", appUserId, "keyword1"),
            new UserKeyword("userKeywordId2", appUserId, "keyword2")
        );

        List<Keyword> keywords = Arrays.asList(
                new Keyword("keyword1", "Keyword 1"),
                new Keyword("keyword2", "Keyword 2")
        );

        when(keywordRepository.findByKeywordIdIn(anyList())).thenReturn(keywords);

        List<Keyword> result = userKeywordService.findKeywordsByUserKeywords(userKeywords);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(keywordRepository, times(1)).findByKeywordIdIn(anyList());

    }
}
