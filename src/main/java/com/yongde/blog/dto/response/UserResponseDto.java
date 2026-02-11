package com.yongde.blog.dto.response;

import com.yongde.blog.enums.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt
) {
}
