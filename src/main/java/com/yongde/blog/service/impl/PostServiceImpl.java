package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.PostRepository;
import com.yongde.blog.service.PostService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {
        Post newPost = postMapper.toEntity(createPostRequestDto);
        postRepository.save(newPost);
        return postMapper.toDto(newPost);
    }

    @Override
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                // equivalent to post -> postMapper.toDto(post) which basically means for
                // each post in posts, convert it to a PostResponseDto using the postMapper.
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostResponseDto getPost(Long postId) {
        // orElseThrow() takes in an exceptionSupplier, basically a functional interface that will get executed if needed.
        // here instead of constructing an exceptionSupplier, we use lambda expression.
        // in the background, the compiler converts this lambda expression into a supplier object. it uses target typing to infer.
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return postMapper.toDto(post);
    }

    @Override
    public PostResponseDto updatePost(Long postId, CreatePostRequestDto createPostRequestDto) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        post.setTitle(createPostRequestDto.title());
        post.setContent(createPostRequestDto.content());
        post.setCategory(createPostRequestDto.category());
        post.setTags(createPostRequestDto.tags());
        post.setUpdated(Instant.now());

        return postMapper.toDto(post);

    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        postRepository.delete(post);
    }
}
