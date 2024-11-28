package com.nyanmyohtet.springbootstarter.service.impl;

import com.nyanmyohtet.springbootstarter.exception.BadRequestException;
import com.nyanmyohtet.springbootstarter.exception.UserNotFoundException;
import com.nyanmyohtet.springbootstarter.model.User;
import com.nyanmyohtet.springbootstarter.repository.UserRepository;
import com.nyanmyohtet.springbootstarter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user with encoded password.
     *
     * @param username    the username
     * @param email       the email
     * @param rawPassword the raw password
     */
    @Override
    public void registerUser(String username, String email, String rawPassword) {
        validateUserDetails(username, email);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = createUser(username, email, encodedPassword);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    /**
     * Find user by username.
     *
     * @param username the username
     * @return an Optional containing the User if found
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email.
     *
     * @param email the email
     * @return an Optional containing the User if found
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    private void validateUserDetails(String username, String email) {
        if (existsByUsername(username) || existsByEmail(email)) {
            throw new BadRequestException("Username or Email is already taken!");
        }
    }

    private User createUser(String username, String email, String encodedPassword) {
        return User.builder()
                .username(username)
                .email(email)
                .passwordHash(encodedPassword)
                .enabled(true)
                .build();
    }
}