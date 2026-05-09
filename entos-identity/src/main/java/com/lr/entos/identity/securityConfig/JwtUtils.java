package com.lr.entos.identity.securityConfig;

import com.lr.entos.identity.securityConfig.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;

    private SecretKey key(){
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(
                StandardCharsets.UTF_8
        ));
    }

    public String generateToken(String email, Map<String, Object> extractClaims ){
        return Jwts.builder()
                .claims(extractClaims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
                .signWith(key())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // --- SPECIFIC CLAIMS ---

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String extractAvatar(String token) {
        return extractAllClaims(token).get("avatar", String.class);
    }

    // --- VALIDATION ---

    public boolean validateToken(String token) {
        try {
            // This line validates signature AND expiration automatically
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

}
