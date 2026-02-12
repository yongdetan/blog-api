package com.yongde.blog.mapper;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequestDto createUserRequestDto) {
        return new User(createUserRequestDto.firstName(), createUserRequestDto.lastName(),
                createUserRequestDto.email(), createUserRequestDto.password());
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
