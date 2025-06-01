package com.example.conduit.security;

import com.example.conduit.modules.user.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + 1000 * 60 * 60 * 24); // 24 hours
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .setIssuer("conduit")
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public void addUserToToken(User user) {
        String username = user.getUsername();
        String jwtToken = generateToken(username);
        user.setToken(jwtToken);
    }
}
