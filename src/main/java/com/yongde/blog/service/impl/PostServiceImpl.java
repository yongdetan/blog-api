package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import com.yongde.blog.entity.User;
import com.yongde.blog.enums.PostStatus;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.PostRepository;
import com.yongde.blog.service.PostService;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User author) {

        Post post = new Post(createPostRequestDto.title(), createPostRequestDto.content(), author);
        post.setCategory(createPostRequestDto.category());
        post.setTags(createPostRequestDto.tags());
        post.setStatus(createPostRequestDto.status());

        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public List<PostResponseDto> getAllPublicPosts() {
        List<Post> publicPosts = postRepository.findAllByStatus(PostStatus.PUBLIC);

        return publicPosts.stream()
                // equivalent to post -> postMapper.toDto(post) which basically means for
                // each post in posts, convert it to a PostResponseDto using the postMapper.
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostResponseDto> getAllAuthoredPosts(User author) {
        List<Post> authoredPosts = postRepository.findAllByAuthorId(author.getId());

        return authoredPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostResponseDto getPost(Long postId, User author) {
        // orElseThrow() takes in an exceptionSupplier, basically a functional interface that will get executed if needed.
        // here instead of constructing an exceptionSupplier, we use lambda expression.
        // in the background, the compiler converts this lambda expression into a supplier object. it uses target typing to infer.
        Post post = postRepository.findPostByIdAndAuthorId(postId, author.getId())
                .orElseThrow(() -> new PostNotFoundException(postId));
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
        post.setStatus(createPostRequestDto.status());
        post.setUpdatedAt(Instant.now());

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
