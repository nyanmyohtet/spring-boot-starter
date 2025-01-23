package com.nyanmyohtet.springbootstarter.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    /**
     * Generate JWT token for a given username.
     *
     * @param username the username for which the token is generated
     * @return the JWT token as a String
     */
    public String generate(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(calculateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, jwtSecret)
                .compact();
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
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new UnsupportedJwtException("Invalid JWT signature");
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
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
