package com.shoes.identity.service.Impl;

import com.shoes.identity.dto.LoginRequest;
import com.shoes.identity.entity.Role;
import com.shoes.identity.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secretKey;
    @Value("${app.jwt.expiration-ms}")
    private Long expiration;
    public String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email",String.class);
    }
}
