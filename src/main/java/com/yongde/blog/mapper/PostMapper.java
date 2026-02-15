package com.yongde.blog.mapper;

import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory(),
                post.getTags(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getAuthor().getId()
        );
    }


}
