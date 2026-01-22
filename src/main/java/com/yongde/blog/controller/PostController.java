package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Adhere to api naming convention
@RestController
@RequestMapping(path = "api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto
    ) {
        PostResponseDto postResponseDto = postService.createPost(createPostRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping (path = "/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
    ){
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok(postResponseDto);
    }
}
