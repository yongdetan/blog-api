package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

}
