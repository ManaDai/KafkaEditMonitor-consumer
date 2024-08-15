package com.example.wikimedia_consumer.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wikimedia_consumer.config.KafkaConsumerManager;
import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.service.AppUserService;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private KafkaConsumerManager kafkaConsumerManager;

    private MockHttpSession session;
    private AppUser mockAppUser;

    @BeforeEach
    void setUp() {
        mockAppUser = new AppUser();
        mockAppUser.setAppUserId("1L");
        session = new MockHttpSession();

    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    void testToLogin_sucess() throws Exception {

        when(appUserService.findAppUser(anyString(), anyString())).thenReturn(mockAppUser);

        mockMvc.perform(post("/login/to-login")
                .param("username", "testuser")
                .param("password", "testpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

    }
}
