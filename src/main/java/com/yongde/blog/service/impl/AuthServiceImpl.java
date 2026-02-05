package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.entity.User;
import com.yongde.blog.entity.UserPrincipal;
import com.yongde.blog.repository.UserRepository;
import com.yongde.blog.service.AuthService;
import com.yongde.blog.service.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String verifyAccount(LoginRequestDto loginRequestDto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.email(),
                                loginRequestDto.password()
                        )
                );

        if (authentication.isAuthenticated()){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return jwtService.generateToken(userPrincipal.getId());
        }

        return "";

    }
}
