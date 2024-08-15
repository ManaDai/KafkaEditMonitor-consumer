package com.example.wikimedia_consumer.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wikimedia_consumer.service.AppUserService;

@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;


    @Test
    void testAddUser_success() throws Exception {
        String username = "testuser";
        String email = "test@example.com";
        String password = "testpassword";

        when(appUserService.isAlreadyRegisteredByEmail(anyString())).thenReturn(false);

        mockMvc.perform(post("/register/add-user")
                .param("username", username)
                .param("email", email)
                .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAddUser_ValidationErrorToAddUser() throws Exception {

        String username = null;
        String email = "test@example.com";
        String password = "testpassword";

        mockMvc.perform(post("/register/add-user")
                .param("username", username)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("user-registration"))
                .andExpect(model().attributeHasFieldErrors("userRegisterForm", "username"));

    }

    @Test
    void testAddUser_RegisteredUserFailToAddUser() throws Exception {

        when(appUserService.isAlreadyRegisteredByEmail(anyString())).thenReturn(true);

        String username = "username";
        String email = "test@example.com";
        String password = "testpassword";

        mockMvc.perform(post("/register/add-user")
                .param("username", username)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("user-registration"))
                .andExpect(model().attribute("error", "すでに登録済みのメールアドレスです。")); // エラーメッセー
    }

    @Test
    void testToRegister() throws Exception {

        mockMvc.perform(get("/register/to-register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-registration"));

    }
}
