package com.nyanmyohtet.springbootstarter.service;

import com.nyanmyohtet.springbootstarter.model.RefreshToken;
import com.nyanmyohtet.springbootstarter.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    void verifyExpiration(RefreshToken token);
    void deleteByUser(User user);
}
