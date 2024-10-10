package com.nyanmyohtet.springbootstarter.service.impl;

import com.nyanmyohtet.springbootstarter.exception.TokenRefreshException;
import com.nyanmyohtet.springbootstarter.model.RefreshToken;
import com.nyanmyohtet.springbootstarter.model.User;
import com.nyanmyohtet.springbootstarter.repository.RefreshTokenRepository;
import com.nyanmyohtet.springbootstarter.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Create a new refresh token for a user.
     *
     * @param user the user for whom to create the token
     * @return the created RefreshToken
     */
    @Override
    public RefreshToken createRefreshToken(User user) {
        return refreshTokenRepository.save(buildRefreshToken(user));
    }

    /**
     * Find a refresh token by its token string.
     *
     * @param token the token string
     * @return Optional containing the RefreshToken if found
     */
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verify the expiration of a refresh token.
     *
     * @param token the RefreshToken to verify
     * @throws TokenRefreshException if the token is expired
     */
    @Override
    public void verifyExpiration(RefreshToken token) {
        if (isExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }
    }

    /**
     * Delete all refresh tokens for a user.
     *
     * @param user the user whose tokens to delete
     */
    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    private RefreshToken buildRefreshToken(User user) {
        return RefreshToken.builder()
                .user(user)
                .token(generateUniqueToken())
                .expiryDate(calculateExpiryDate())
                .build();
    }

    private String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }

    private Instant calculateExpiryDate() {
        return Instant.now().plusMillis(refreshTokenDurationMs);
    }

    private boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}