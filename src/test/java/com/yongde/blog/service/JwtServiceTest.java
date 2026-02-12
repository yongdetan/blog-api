package com.yongde.blog.service;

import com.yongde.blog.service.impl.JWTServiceImpl;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JwtServiceTest {

    private static final String SECRET =
            "test-secret-key-that-is-at-least-256-bits-long-for-testing";

    private JWTServiceImpl jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JWTServiceImpl(SECRET);
    }

    @Test
    public void constructor_nullSecret_throwsIllegalStateException() {
        Assertions.assertThatThrownBy(() -> new JWTServiceImpl(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET environment variable must be set.");
    }

    @Test
    public void constructor_blankSecret_throwsIllegalStateException() {
        Assertions.assertThatThrownBy(() -> new JWTServiceImpl(" "))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET environment variable must be set.");
    }

    @Test
    public void getExpirationMs_returns24Hours() {
        Assertions.assertThat(jwtService.getExpirationMs())
                .isEqualTo(1000 * 60 * 60 * 24);
    }

    @Test
    public void generateToken_validUserId_containsCorrectSubject() {
        Long id =1L;

        String token = jwtService.generateToken(id);

        Assertions.assertThat(token).isNotBlank();

        Claims claims = jwtService.validateTokenAndReturnClaims(token);
        Assertions.assertThat(claims.getSubject()).isEqualTo(String.valueOf(id));

    }

    @Test
    public void generateToken_validUserId_setCorrectExpiration() {
        Long id =1L;

        String token = jwtService.generateToken(id);

        Assertions.assertThat(token).isNotBlank();

        Claims claims = jwtService.validateTokenAndReturnClaims(token);


        Date issuedDate = claims.getIssuedAt();
        Date expiredDate = claims.getExpiration();
        Long expirationDuration = expiredDate.getTime() - issuedDate.getTime();

        Assertions.assertThat(expiredDate).isAfter(issuedDate);

        Assertions.assertThat(expirationDuration)
                .isBetween(
                        jwtService.getExpirationMs() - 5,
                        jwtService.getExpirationMs() + 5
                );
    }

    @Test
    void validateToken_differentSecret_throwsException() {
        Long id = 1L;
        JWTServiceImpl jwtService1 = new JWTServiceImpl(SECRET);
        JWTServiceImpl jwtService2 =
                new JWTServiceImpl("another-secret-key-that-is-also-256-bits-long");

        String token = jwtService1.generateToken(id);

        Assertions.assertThatThrownBy(() ->
                jwtService2.validateTokenAndReturnClaims(token)
        ).isInstanceOf(Exception.class);
    }


}
