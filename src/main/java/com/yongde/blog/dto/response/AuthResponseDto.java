package com.yongde.blog.dto.response;

public record AuthResponseDto(
        String token,
        String type,
        Long expiresIn,
        Long userId
) {
}
