package com.example.wikimedia_consumer.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.domain.Keyword;
import com.example.wikimedia_consumer.domain.UserKeyword;
import com.example.wikimedia_consumer.service.UserKeywordService;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserKeywordService userKeywordService;

    private MockHttpSession session;
    private AppUser mockAppUser;
    private List<UserKeyword> mockUserKeywordList;
    private List<Keyword> mockKeywordList;

    @BeforeEach
    void setUp() {
        mockAppUser = new AppUser();
        mockAppUser.setAppUserId("1L");

        session = new MockHttpSession();
        session.setAttribute("appUser", mockAppUser);

        UserKeyword userKeyword1 = new UserKeyword();
        userKeyword1.setKeywordId("1L");
        mockUserKeywordList = Collections.singletonList(userKeyword1);

        Keyword keyword1 = new Keyword();
        keyword1.setKeywordId("1L");
        keyword1.setKeyword("TestKeyword");
        mockKeywordList = Collections.singletonList(keyword1);
    }

    @Test
    void whenUserIsNotLoggedIn_thenRedirectToLogin() throws Exception {
        session.setAttribute("appUser", null); // ログインしていない状態をシミュレート

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void whenNoUserKeywords_thenShowDashboardWithNoKeywords() throws Exception {
        when(userKeywordService.findByAppUserId(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void whenUserHasKeywords_thenShowDashboardWithKeywords() throws Exception {
        when(userKeywordService.findByAppUserId(anyString())).thenReturn(mockUserKeywordList);
        when(userKeywordService.findKeywordsByUserKeywords(mockUserKeywordList)).thenReturn(mockKeywordList);

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("keywords"))
                .andExpect(model().attribute("keywords", mockKeywordList));
    }
}
