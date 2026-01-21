package com.yongde.blog.dto.response;

import java.time.Instant;
import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String category,
        List<String> tags,
        Instant created,
        Instant updated
) {
}
