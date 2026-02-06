package com.yongde.blog.service;

import io.jsonwebtoken.Claims;

public interface JWTService {

    String generateToken(Long userId);

    Claims validateTokenAndReturnClaims(String token);

    long getExpirationMs();
}
