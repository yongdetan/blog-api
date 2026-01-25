package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.service.PostService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postResponseDto.id())
                .toUri();

        System.out.println(location);
        return ResponseEntity.created(location).body(postResponseDto);
    }
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
    ){
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto
    ){
        PostResponseDto postResponseDto = postService.updatePost(postId, createPostRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }
}
