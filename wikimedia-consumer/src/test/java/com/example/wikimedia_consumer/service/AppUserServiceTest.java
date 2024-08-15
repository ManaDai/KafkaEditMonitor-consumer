package com.example.wikimedia_consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.repository.AppUserRepository;

@ExtendWith(MockitoExtension.class) // Mockitoの初期化を行う
public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void testFindAppUser() {

        AppUser testAppUser = new AppUser();
        testAppUser.setAppUserId("123");
        testAppUser.setUsername("test");
        testAppUser.setPassword("password");
        testAppUser.setEmail("test@com");

        when(appUserRepository.findByUsernameAndPassword("test", "password")).thenReturn(testAppUser);

        AppUser result = appUserService.findAppUser("test", "password");

        assertNotNull(result);
        assertEquals("test", result.getUsername());
        assertEquals("password", result.getPassword());

    }

    @Test
    void testIsAlreadyRegisteredByEmail() {

        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("test");
        testAppUser.setEmail("test@com");

        when(appUserRepository.findByEmail("test@com")).thenReturn(testAppUser);
        Boolean result = appUserService.isAlreadyRegisteredByEmail("test@com");
        assertTrue(result);

        when(appUserRepository.findByEmail("nonexistent@com")).thenReturn(null);
        Boolean resultForNonExistentEmail = appUserService.isAlreadyRegisteredByEmail("nonexistent@com");
        assertFalse(resultForNonExistentEmail);

    }

    @Test
    void testSaveAppUser() {

        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("test");
        testAppUser.setPassword("password");
        testAppUser.setEmail("test@com");

        when(appUserRepository.save(testAppUser)).thenReturn(testAppUser);

        AppUser result = appUserService.saveAppUser(testAppUser);

        assertNotNull(result);
        verify(appUserRepository, times(1)).save(testAppUser);

    }

}