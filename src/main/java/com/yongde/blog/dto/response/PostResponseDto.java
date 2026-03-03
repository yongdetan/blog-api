package com.yongde.blog.dto.response;

import com.yongde.blog.entity.Tag;
import com.yongde.blog.enums.PostStatus;

import java.time.Instant;
import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String categoryName,
        List<Tag> tags,
        PostStatus postStatus,
        Instant created,
        Instant updated,
        Long authorId
) {
}
