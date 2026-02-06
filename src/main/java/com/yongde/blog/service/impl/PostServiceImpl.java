package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import com.yongde.blog.entity.User;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.PostRepository;
import com.yongde.blog.repository.UserRepository;
import com.yongde.blog.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Transactional
    @Override
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User author) {

        Post post = new Post(createPostRequestDto.title(), createPostRequestDto.content(), author);
        post.setCategory(createPostRequestDto.category());
        post.setTags(createPostRequestDto.tags());

        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
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

    @Transactional
    @Override
    public PostResponseDto updatePost(Long postId, CreatePostRequestDto createPostRequestDto, User author) {

        Post post = postRepository.findPostByIdAndAuthorId(postId, author.getId())
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.setTitle(createPostRequestDto.title());
        post.setContent(createPostRequestDto.content());
        post.setCategory(createPostRequestDto.category());
        post.setTags(createPostRequestDto.tags());
        post.setUpdated(Instant.now());

        Post updatedPost = postRepository.save(post);
        return postMapper.toDto(updatedPost);

    }

    @Transactional
    @Override
    public void deletePost(Long postId, User author) {
        Post post = postRepository.findPostByIdAndAuthorId(postId, author.getId())
                .orElseThrow(() -> new PostNotFoundException(postId));
        postRepository.delete(post);
    }
}
