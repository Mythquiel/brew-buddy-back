package com.brewbuddy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${auth.jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates a JWT token and returns the claims if valid
     */
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extracts user ID from JWT claims
     */
    public UUID getUserId(Claims claims) {
        String sub = claims.getSubject();
        if (sub == null) {
            return null;
        }
        try {
            return UUID.fromString(sub);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid user ID format in JWT: {}", sub);
            return null;
        }
    }

    /**
     * Extracts username from JWT claims
     */
    public String getUsername(Claims claims) {
        return claims.get("username", String.class);
    }

    /**
     * Extracts email from JWT claims
     */
    public String getEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    /**
     * Extracts roles from JWT claims
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            return (List<String>) rolesObj;
        }
        return List.of();
    }

    /**
     * Checks if token is expired
     */
    public boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }
}
