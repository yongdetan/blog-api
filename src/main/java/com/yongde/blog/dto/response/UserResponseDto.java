package com.yongde.blog.dto.response;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
}
