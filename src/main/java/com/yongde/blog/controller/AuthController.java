package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.service.AuthService;
import com.yongde.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto
    ){
        UserResponseDto userResponseDto = userService.createUser(createUserRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(userResponseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(userResponseDto);
    }

    @PostMapping(path = "/login")
    public String login(
            @RequestBody LoginRequestDto loginRequestDto
    ){
        return authService.verifyAccount(loginRequestDto);
    }

}
