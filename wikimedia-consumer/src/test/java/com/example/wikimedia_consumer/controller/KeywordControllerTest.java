package com.example.wikimedia_consumer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.service.KeywordService;
import com.example.wikimedia_consumer.service.UserKeywordService;

@WebMvcTest(KeywordController.class)
public class KeywordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeywordService keywordService;

    @MockBean
    private UserKeywordService userKeywordService;

    private MockHttpSession session;
    private AppUser mockAppUser;

    @BeforeEach
    void setUp() {
        mockAppUser = new AppUser();
        mockAppUser.setAppUserId("1L");
        session = new MockHttpSession();
        session.setAttribute("appUser", mockAppUser);

    }

    @Test
    void testAddKeyword_alreadyRegistered() throws Exception {
        List<String> userKeywordIds = List.of("1", "2", "3");
        List<String> registeredStringKeywords = List.of("keyword1", "keyword2", "keyword3");
        when(userKeywordService.findKeywordIdsByUserId(anyString())).thenReturn(userKeywordIds);
        when(keywordService.findKeywordsByKeywordIds(anyList())).thenReturn(registeredStringKeywords);

        mockMvc.perform(post("/keyword/add-keyword")
                .session(session)
                .param("keyword-input", "keyword1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dashboard"));

    }

    @Test
    void testAddKeyword_success() throws Exception {
        List<String> userKeywordIds = List.of("1", "2", "3");
        List<String> registeredStringKeywords = List.of("keyword2", "keyword3");

        when(userKeywordService.findKeywordIdsByUserId(anyString())).thenReturn(userKeywordIds);
        when(keywordService.findKeywordsByKeywordIds(anyList())).thenReturn(registeredStringKeywords);
        when(keywordService.saveKeyword(any(Keyword.class))).thenReturn(new Keyword("4", "newKeyword"));

        mockMvc.perform(post("/keyword/add-keyword")
                .session(session)
                .param("keyword-input", "newKeyword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/dashboard"));
    }



    @Test
    void testDeleteKeyword_success() throws Exception {
        Keyword keyword = new Keyword("1", "keyword1");
        when(keywordService.findById("1")).thenReturn(keyword);

        doNothing().when(userKeywordService).deleteByKeywordId("1");
        doNothing().when(keywordService).deleteById("1");

        mockMvc.perform(delete("/keyword/delete-keyword/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
