package ru.vgerasimov.booking.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vgerasimov.booking.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenService {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.ttl-seconds}")
    private Long ttlSeconds;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        log.info("Generating JWT token for user: {}", user.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("username", user.getUsername());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ttlSeconds * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("Token generated for user: {}", user.getUsername());
        return token;
    }
}
