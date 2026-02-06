package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.dto.response.AuthResponseDto;
import com.yongde.blog.entity.UserPrincipal;
import com.yongde.blog.service.AuthService;
import com.yongde.blog.service.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto verifyAccount(LoginRequestDto loginRequestDto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.email(),
                                loginRequestDto.password()
                        )
                );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String token = jwtService.generateToken(userPrincipal.getId());

        return new AuthResponseDto(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                userPrincipal.getId()
        );
    }
}
