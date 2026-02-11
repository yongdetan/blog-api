package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.UserPrincipal;
import com.yongde.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        PostResponseDto postResponseDto = postService.createPost(createPostRequestDto, principal.user());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postResponseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(postResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPublicPosts() {
        List<PostResponseDto> posts = postService.getAllPublicPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<List<PostResponseDto>> getAllAuthoredPosts(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<PostResponseDto> authoredPosts = postService.getAllAuthoredPosts(userPrincipal.user());
        return ResponseEntity.ok(authoredPosts);
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        PostResponseDto postResponseDto = postService.getPost(postId, userPrincipal.user());
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        PostResponseDto postResponseDto = postService.updatePost(postId, createPostRequestDto, userPrincipal.user());
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId
    ){
        postService.deletePost(postId, userPrincipal.user());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
