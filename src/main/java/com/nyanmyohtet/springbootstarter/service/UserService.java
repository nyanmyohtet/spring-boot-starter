package com.nyanmyohtet.springbootstarter.service;

import com.nyanmyohtet.springbootstarter.model.User;

import java.util.Optional;

public interface UserService {
    void registerUser(String username, String email, String rawPassword);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
