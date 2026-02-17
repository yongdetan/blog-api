package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Category;
import com.yongde.blog.entity.Post;
import com.yongde.blog.entity.User;
import com.yongde.blog.enums.PostStatus;
import com.yongde.blog.exception.CategoryNotFoundException;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User author) {

        //check to see if category exists. throw exception if it does not.
        Category category = categoryRepository.findById(createPostRequestDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(createPostRequestDto.categoryId()));

        Post post = new Post(createPostRequestDto.title(), createPostRequestDto.content(), author);
        post.setCategory(category);
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

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // authors can always view their own posts regardless of status
        if (author != null && author.equals(post.getAuthor())) {
            return postMapper.toDto(post);
        }

        // non-author can only view public post.
        if(post.getStatus() != PostStatus.PUBLIC) {
            throw new PostNotFoundException(postId);
        }

        return postMapper.toDto(post);
    }

    @Transactional
    @Override
    public PostResponseDto updatePost(Long postId, CreatePostRequestDto createPostRequestDto, User author) {

        Post post = postRepository.findPostByIdAndAuthorId(postId, author.getId())
                .orElseThrow(() -> new PostNotFoundException(postId));

        Category category = categoryRepository.findById(createPostRequestDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(createPostRequestDto.categoryId()));

        post.setTitle(createPostRequestDto.title());
        post.setContent(createPostRequestDto.content());
        post.setCategory(category);
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
