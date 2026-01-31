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
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

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
                "post",
                null,
                null,
                null
        );

        Post post = Post.builder()
                .title("post")
                .build();

        Post savedPost = Post.builder()
                .title("post")
                .build();

        PostResponseDto postResponseDto = new PostResponseDto(
                1L,
                "post",
                null,
                null,
                null,
                null,
                null
        );
        when(postMapper.toEntity(createPostRequestDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(savedPost);
        when(postMapper.toDto(savedPost)).thenReturn(postResponseDto);

        //Act
        PostResponseDto result = postService.createPost(createPostRequestDto);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("post");

        verify(postMapper).toEntity(createPostRequestDto);
        verify(postRepository).save(post);
        verify(postMapper).toDto(savedPost);
    }

    @Test
    public void PostService_GetAllPosts_ReturnsListOfPostResponseDto() {
        //Arrange
        Post post1 = Post.builder().title("post1").build();
        Post post2 = Post.builder().title("post2").build();

        PostResponseDto postResponseDto1 = new PostResponseDto(1L, "post1", null, null, null, null, null );
        PostResponseDto postResponseDto2 = new PostResponseDto(2L, "post2", null, null, null, null, null );

        when(postRepository.findAll()).thenReturn(List.of(post1,post2));
        when(postMapper.toDto(post1)).thenReturn(postResponseDto1);
        when(postMapper.toDto(post2)).thenReturn(postResponseDto2);

        //Act
        List<PostResponseDto> posts = postService.getAllPosts();

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

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
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

    @Test
    public void PostService_UpdatePost_ReturnsPostResponseDto() {
        Long postId = 1L;

        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "post",
                null,
                null,
                null
        );

        Post post = Post.builder().title("post").build();

        Post updatedPost = Post.builder().title("updated_post").build();

        PostResponseDto updatedDto = new PostResponseDto(
                1L,
                "updated_post",
                null,
                null,
                null,
                null,
                null
        );

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(updatedPost);
        when(postMapper.toDto(updatedPost)).thenReturn(updatedDto);

        PostResponseDto result = postService.updatePost(postId, createPostRequestDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo(updatedDto.title());

        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
        verify(postMapper).toDto(updatedPost);
    }

    @Test
    public void PostService_UpdatePost_ThrowsPostNotFoundException() {
        Long postId = 1L;

        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "post",
                null,
                null,
                null
        );

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.updatePost(postId, createPostRequestDto))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findById(postId);
        // check that postMapper is not called. if exception is raised, postMapper should not be called.
        verifyNoInteractions(postMapper);
    }

    @Test
    public void PostService_DeletePost_DeletesSuccessfully() {
        Long postId = 1L;

        Post post = Post.builder().title("post").build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId);

        verify(postRepository).findById(postId);
        verify(postRepository).delete(post);
    }

    @Test
    public void PostService_DeletePost_ThrowsPostNotFoundException() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        // verify that delete method is not called
        // do not use verifyNoInteractions unlike the previous unit tests because it requires a mock object.
        verify(postRepository, never()).delete(any());
    }

}
