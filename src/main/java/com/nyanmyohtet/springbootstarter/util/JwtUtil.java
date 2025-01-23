package com.nyanmyohtet.springbootstarter.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * Generate JWT token for a given username.
     *
     * @param username the username for which the token is generated
     * @return the JWT token as a String
     */
    public String generate(String username) {
        try {
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(calculateExpirationDate())
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            logger.error("Error occur while generating JWT: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Extract username from JWT token.
     *
     * @param token the JWT token
     * @return the username as a String
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Validate the JWT token.
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new UnsupportedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new UnsupportedJwtException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new UnsupportedJwtException("JWT claims string is empty");
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Calculate expiration date based on current time and expiration duration.
     *
     * @return the expiration date as a Date object
     */
    private Date calculateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtExpirationMs);
    }

    /**
     * Parse claims from the JWT token.
     *
     * @param token the JWT token
     * @return Claims object containing the claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
