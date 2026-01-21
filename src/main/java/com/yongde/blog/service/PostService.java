package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(CreatePostRequestDto createPostRequestDto);

    List<PostResponseDto> listPosts();

    PostResponseDto updatePost(Long postId, CreatePostRequestDto createPostRequestDto);

    void deletePost(Long postId);
}
