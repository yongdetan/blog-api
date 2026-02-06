package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.User;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User author);

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPost(Long postId);

    PostResponseDto updatePost(Long postId, CreatePostRequestDto createPostRequestDto, User author);

    void deletePost(Long postId, User author);
}
