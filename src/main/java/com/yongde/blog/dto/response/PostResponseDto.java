package com.yongde.blog.dto.response;

import com.yongde.blog.enums.PostStatus;

import java.time.Instant;
import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String categoryName,
        List<String> tags,
        PostStatus postStatus,
        Instant created,
        Instant updated,
        Long authorId
) {
}
