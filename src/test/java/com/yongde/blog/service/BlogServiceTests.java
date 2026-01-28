package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.PostRepository;
import com.yongde.blog.service.impl.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void PostService_CreatePost_ReturnsPostResponseDto() {

        //Arrange
        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "First Post",
                "Hello World!",
                "Tech",
                List.of("First", "Post")
        );

        Post post = Post.builder()
                .title("First Post")
                .content("Hello World!")
                .category("Tech")
                .tags(List.of("First", "Post"))
                .build();

        Post savedPost = Post.builder()
                .title("First Post")
                .content("Hello World!")
                .category("Tech")
                .tags(List.of("First", "Post"))
                .build();

        PostResponseDto postResponseDto = new PostResponseDto(
                1L,
                "First Post",
                "Hello World!",
                "Tech",
                List.of("First", "Post"),
                Instant.now(),
                Instant.now()
        );
        when(postMapper.toEntity(createPostRequestDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(savedPost);
        when(postMapper.toDto(savedPost)).thenReturn(postResponseDto);

        //Act
        PostResponseDto result = postService.createPost(createPostRequestDto);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("First Post");

        verify(postMapper).toEntity(createPostRequestDto);
        verify(postRepository).save(post);
        verify(postMapper).toDto(savedPost);
    }

    @Test
    public void PostService_GetPosts_ReturnsListOfPostResponseDto() {
        //Arrange
        Post post1 = Post.builder().title("post1").build();
        Post post2 = Post.builder().title("post2").build();

        PostResponseDto postResponseDto1 = new PostResponseDto(1L, "post1", null, null, null, null, null );
        PostResponseDto postResponseDto2 = new PostResponseDto(2L, "post2", null, null, null, null, null );

        when(postRepository.findAll()).thenReturn(List.of(post1,post2));
        when(postMapper.toDto(post1)).thenReturn(postResponseDto1);
        when(postMapper.toDto(post2)).thenReturn(postResponseDto2);

        //Act
        List<PostResponseDto> posts = postService.getPosts();

        //Assert
        Assertions.assertThat(posts).hasSize(2);
        Assertions.assertThat(posts.get(0).title()).isEqualTo("post1");
        Assertions.assertThat(posts.get(1).title()).isEqualTo("post2");

        verify(postRepository).findAll();
        verify(postMapper).toDto(post1);
        verify(postMapper).toDto(post2);
    }

    @Test
    public void PostService_GetPost_ReturnsPostResponseDto() {
        Post post = Post.builder().title("post").build();
        PostResponseDto postResponseDto = new PostResponseDto(1L, "post", null, null, null, null, null );

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));
        when(postMapper.toDto(post)).thenReturn(postResponseDto);

        PostResponseDto result = postService.getPost(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("post");

        verify(postRepository).findById(1L);
        verify(postMapper).toDto(post);
    }

    @Test
    public void PostService_GetPost_ThrowsPostNotFoundException() {
        Long postId = 1L;

        // cannot use null or anything similar. must use optional because findById returns optional
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findById(postId);
        verifyNoInteractions(postMapper);
    }
}
