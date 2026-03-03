package com.yongde.blog.dto.response;

import java.time.Instant;

public record TagResponseDto(

        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
