package com.episen.membership.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.episen.membership.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    void deleteByUsername(String username);
}