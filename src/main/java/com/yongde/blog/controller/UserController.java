package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
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
}
