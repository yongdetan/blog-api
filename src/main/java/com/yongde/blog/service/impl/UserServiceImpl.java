package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.entity.User;
import com.yongde.blog.mapper.UserMapper;
import com.yongde.blog.repository.UserRepository;
import com.yongde.blog.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    public final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        //TODO encrypt the password
        User user = userMapper.toEntity(createUserRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
