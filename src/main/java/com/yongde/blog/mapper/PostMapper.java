package com.yongde.blog.mapper;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toEntity(CreatePostRequestDto createPostRequestDto) {
        return new Post(
                createPostRequestDto.title(),
                createPostRequestDto.content(),
                createPostRequestDto.category(),
                createPostRequestDto.tags()
        );
    }

    public PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory(),
                post.getTags(),
                post.getCreated(),
                post.getUpdated()
        );
    }


}
