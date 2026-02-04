package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.entity.User;
import com.yongde.blog.exception.EmailExistsException;
import com.yongde.blog.mapper.UserMapper;
import com.yongde.blog.repository.UserRepository;
import com.yongde.blog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {

        // Check whether email already exists in database
        // Flag it here instead of in the controller because if we create our own custom validation, we need to inject repository which would go against layered architecture
        if (userRepository.findByEmail(createUserRequestDto.email()).isPresent()) {
            throw new EmailExistsException(createUserRequestDto.email());
        }

        User user = userMapper.toEntity(createUserRequestDto);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
