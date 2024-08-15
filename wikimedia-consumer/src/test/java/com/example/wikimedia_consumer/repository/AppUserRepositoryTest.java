package com.example.wikimedia_consumer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.wikimedia_consumer.domain.AppUser;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AppUserRepositoryTest {
 
    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setUsername("testuser");
        testUser.setPassword("testpassword");
        testUser.setEmail("test@example.com");
        appUserRepository.save(testUser);
    }

    @AfterEach
    void cleanUp() {
        appUserRepository.delete(testUser);
    }

    @Test
    void testFindByUsernameAndPassword() {
        AppUser foundUser = appUserRepository.findByUsernameAndPassword("testuser", "testpassword");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("testpassword", foundUser.getPassword());
    }

    @Test
    void testFindByEmail() {
        AppUser foundUser = appUserRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("testpassword", foundUser.getPassword());
    }


}
