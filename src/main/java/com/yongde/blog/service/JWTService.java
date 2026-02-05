package com.yongde.blog.service;

public interface JWTService {

    String generateToken(Long userId);

    String extractEmail(String token);

}
