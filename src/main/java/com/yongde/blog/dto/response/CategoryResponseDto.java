package com.yongde.blog.dto.response;

import com.yongde.blog.entity.Post;

import java.time.Instant;
import java.util.List;

public record CategoryResponseDto(

        Long id,
        String name,
        String description,
        List<Post> posts,
        Instant createdAt,
        Instant updatedAt
) {
}
