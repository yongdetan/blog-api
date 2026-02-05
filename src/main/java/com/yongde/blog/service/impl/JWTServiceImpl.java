package com.yongde.blog.service.impl;

import com.yongde.blog.service.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTServiceImpl implements JWTService {

    private final SecretKey KEY;
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; //24 hours
    public JWTServiceImpl() {
        String secret = System.getenv("JWT_SECRET"); //using env variable to store our secret key

        //ensure that we have set the env variable
        if (secret == null){
            throw new IllegalStateException(
                    "JWT_SECRET environment variable must be set."
            );
        };

        this.KEY = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes());
    }


    @Override
    public String generateToken(Long userId) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims().add(claims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .and()
                .signWith(KEY).compact();
    }

    @Override
    public String extractEmail(String token) {
        return "";
    }
}
