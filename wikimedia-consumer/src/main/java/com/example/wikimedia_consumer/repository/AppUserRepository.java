package com.example.wikimedia_consumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.wikimedia_consumer.domain.AppUser;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String>{

    AppUser findByUsernameAndPassword(String username, String password);

    AppUser findByEmail(String email);



}
