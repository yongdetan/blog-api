package com.yongde.blog.service;

import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.dto.response.AuthResponseDto;

public interface AuthService {

    public AuthResponseDto verifyAccount(LoginRequestDto loginRequestDto);

}
