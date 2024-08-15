package com.example.wikimedia_consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wikimedia_consumer.domain.AppUser;
import com.example.wikimedia_consumer.repository.AppUserRepository;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

  

    public AppUser findAppUser(String username, String password) {

        AppUser appUser = appUserRepository.findByUsernameAndPassword(username, password);
        return appUser;

    }

    public AppUser saveAppUser(AppUser appUser) {

        appUserRepository.save(appUser);
        return appUser;

    }

    public boolean isAlreadyRegisteredByEmail(String email) {

        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser != null) {
            return true;
        } else {
            return false;
        }

    }

}
