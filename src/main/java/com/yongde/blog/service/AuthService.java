package com.yongde.blog.service;

import com.yongde.blog.dto.request.LoginRequestDto;

public interface AuthService {

    public String verifyAccount(LoginRequestDto loginRequestDto);

}
