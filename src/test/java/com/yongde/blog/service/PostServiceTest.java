package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreatePostRequestDto;
import com.yongde.blog.dto.response.PostResponseDto;
import com.yongde.blog.entity.Post;
import com.yongde.blog.entity.User;
import com.yongde.blog.enums.PostStatus;
import com.yongde.blog.enums.Role;
import com.yongde.blog.exception.PostNotFoundException;
import com.yongde.blog.mapper.PostMapper;
import com.yongde.blog.repository.PostRepository;
import com.yongde.blog.service.impl.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.*;


// TODO:used @Nested to group similar tests, use @DisplayName to explain what the test is about.
// for getPost(), perhaps I could test more deeply into the 3 scenarios (unauthenticated user viewing nonPublic, authenticated user viewing nonPublic, author viewing nonPublic)
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private User author;

    @BeforeEach
    public void setup() {
        author = new User(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password"
        );
        author.setRole(Role.USER);

        ReflectionTestUtils.setField(author, "id", 1L); //using reflection to set authorId. this is done because id setter is not exposed.

    }

    @Test
    public void createPost_validRequest_returnsPostResponseDto() {

        //Arrange
        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "post",
                "content",
                null,
                null,
                PostStatus.PUBLIC
        );

        Post savedPost = new Post("post", "content", author);

        PostResponseDto postResponseDto = new PostResponseDto(
                1L,
                "post",
                "content",
                null,
                null,
                PostStatus.PUBLIC,
                null,
                null,
                author.getId()
        );

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(postMapper.toDto(savedPost)).thenReturn(postResponseDto);

        //Act
        PostResponseDto result = postService.createPost(createPostRequestDto, author);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("post");
        Assertions.assertThat(result.content()).isEqualTo("content");
        Assertions.assertThat(result.postStatus()).isEqualTo(PostStatus.PUBLIC);
        Assertions.assertThat(result.authorId()).isEqualTo(author.getId());

        verify(postRepository).save(any(Post.class));
        verify(postMapper).toDto(savedPost);
    }

    @Test
    public void getAllPublicPosts_validRequest_returnsListOfPostResponseDto() {
        //Arrange
        Post post1 = new Post("post1", "content1", author);
        Post post2 = new Post("post2", "content2", author);

        PostResponseDto postResponseDto1 = new PostResponseDto(1L, "post1", "content1", null, null, PostStatus.PUBLIC, null, null, author.getId());
        PostResponseDto postResponseDto2 = new PostResponseDto(2L, "post2", "content2", null, null, PostStatus.PUBLIC, null, null, author.getId());

        when(postRepository.findAllByStatus(PostStatus.PUBLIC)).thenReturn(List.of(post1,post2));
        when(postMapper.toDto(post1)).thenReturn(postResponseDto1);
        when(postMapper.toDto(post2)).thenReturn(postResponseDto2);

        //Act
        List<PostResponseDto> posts = postService.getAllPublicPosts();

        //Assert
        Assertions.assertThat(posts)
                .hasSize(2)
                .extracting(
                        PostResponseDto::title,
                        PostResponseDto::content,
                        PostResponseDto::postStatus,
                        PostResponseDto::authorId
                )
                .containsExactly(
                        tuple("post1", "content1", PostStatus.PUBLIC, author.getId()),
                        tuple("post2", "content2", PostStatus.PUBLIC, author.getId())
                );
        verify(postRepository).findAllByStatus(PostStatus.PUBLIC);
        verify(postMapper, times(2)).toDto(any(Post.class));

    }

    @Test
    public void getAllAuthoredPosts_validRequest_returnsListOfPostResponseDto() {
        //Arrange
        Post post1 = new Post("post1", "content1", author);
        Post post2 = new Post("post2", "content2", author);

        PostResponseDto postResponseDto1 = new PostResponseDto(1L, "post1", "content1", null, null, PostStatus.PUBLIC, null, null, author.getId());
        PostResponseDto postResponseDto2 = new PostResponseDto(2L, "post2", "content2", null, null, PostStatus.DRAFT, null, null, author.getId());

        when(postRepository.findAllByAuthorId(author.getId())).thenReturn(List.of(post1,post2));
        when(postMapper.toDto(post1)).thenReturn(postResponseDto1);
        when(postMapper.toDto(post2)).thenReturn(postResponseDto2);

        //Act
        List<PostResponseDto> posts = postService.getAllAuthoredPosts(author);

        //Assert
        Assertions.assertThat(posts)
                .hasSize(2)
                .extracting(
                        PostResponseDto::title,
                        PostResponseDto::content,
                        PostResponseDto::postStatus,
                        PostResponseDto::authorId
                )
                .containsExactly(
                        tuple("post1", "content1", PostStatus.PUBLIC, author.getId()),
                        tuple("post2", "content2", PostStatus.DRAFT, author.getId())
                );

        verify(postRepository).findAllByAuthorId(author.getId());
        verify(postMapper, times(2)).toDto(any(Post.class));

    }

    @Test
    public void getPost_publicPostAndAuthor_returnsPostResponseDto() {
        Post post = new Post("post", "content", author);
        post.setStatus(PostStatus.PUBLIC);
        PostResponseDto postResponseDto = new PostResponseDto(1L, "post", "content", null, null,
                PostStatus.PUBLIC, null, null, author.getId());

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postResponseDto);

        PostResponseDto result = postService.getPost(1L, author);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo("post");
        Assertions.assertThat(result.content()).isEqualTo("content");
        Assertions.assertThat(result.postStatus()).isEqualTo(PostStatus.PUBLIC);
        Assertions.assertThat(result.authorId()).isEqualTo(author.getId());

        verify(postRepository).findById(1L);
        verify(postMapper).toDto(post);
    }

    @Test
    public void getPost_publicPostAndUnauthenticatedUser_returnsPostResponseDto() {
        Post post = new Post("post", "content", author);
        post.setStatus(PostStatus.PUBLIC);
        PostResponseDto postResponseDto = new PostResponseDto(1L, "post", "content", null, null,
                PostStatus.PUBLIC, null, null, author.getId());

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postResponseDto);

        PostResponseDto result = postService.getPost(1L, null);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.postStatus()).isEqualTo(PostStatus.PUBLIC);

        verify(postRepository).findById(1L);
        verify(postMapper).toDto(post);
    }

    @Test
    public void getPost_draftPostAndUnauthenticatedUser_throwsPostNotFoundException() {
        Long postId = 1L;
        Post post = new Post("post", "content", author);
        post.setStatus(PostStatus.DRAFT);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        Assertions.assertThatThrownBy(() -> postService.getPost(postId, null))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findById(postId);
        verifyNoInteractions(postMapper);
    }

    @Test
    public void getPost_draftPostAndNonAuthor_throwsPostNotFoundException() {
        Long postId = 1L;
        User otherUser = new User("De Yong", "Tan", "deyongtan@gmail.com", "password");

        Post post = new Post("post", "content", author);
        post.setStatus(PostStatus.DRAFT);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        Assertions.assertThatThrownBy(() -> postService.getPost(postId, otherUser))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findById(postId);
        verifyNoInteractions(postMapper);
    }

    @Test
    public void getPost_invalidPost_throwsPostNotFoundException() {
        Long postId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.getPost(postId, author))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findById(postId);
        verifyNoInteractions(postMapper);
    }

    @Test
    public void updatePost_validPost_returnsPostResponseDto() {
        Long postId = 1L;

        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "post",
                "content",
                null,
                null,
                PostStatus.PUBLIC
        );

        Post post = new Post("post", "content", author);

        Post updatedPost = new Post("updated_post", "content", author);

        PostResponseDto updatedDto = new PostResponseDto(
                postId,
                "updated_post",
                "updated_content",
                null,
                null,
                PostStatus.PUBLIC,
                null,
                null,
                author.getId()
        );

        when(postRepository.findPostByIdAndAuthorId(postId, author.getId()))
                .thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(updatedPost);
        when(postMapper.toDto(updatedPost)).thenReturn(updatedDto);

        PostResponseDto result = postService.updatePost(postId, createPostRequestDto, author);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo(updatedDto.title());
        Assertions.assertThat(result.content()).isEqualTo(updatedDto.content());
        Assertions.assertThat(result.postStatus()).isEqualTo(updatedDto.postStatus());
        Assertions.assertThat(result.authorId()).isEqualTo(author.getId());

        verify(postRepository).findPostByIdAndAuthorId(postId, author.getId());
        verify(postRepository).save(post);
        verify(postMapper).toDto(updatedPost);
    }

    @Test
    public void updatePost_invalidPost_throwsPostNotFoundException() {
        Long postId = 1L;

        CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                "post",
                "content",
                null,
                null,
                PostStatus.PUBLIC
        );

        when(postRepository.findPostByIdAndAuthorId(postId, author.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.updatePost(postId, createPostRequestDto, author))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        verify(postRepository).findPostByIdAndAuthorId(postId, author.getId());
        verify(postRepository, never()).save(any(Post.class));
        // check that postMapper is not called. if exception is raised, postMapper should not be called.
        verifyNoInteractions(postMapper);
    }

    @Test
    public void deletePost_validRequest_deletesPost() {
        Long postId = 1L;

        Post post = new Post("post", "content", author);

        when(postRepository.findPostByIdAndAuthorId(postId, author.getId()))
                .thenReturn(Optional.of(post));

        postService.deletePost(postId, author);

        verify(postRepository).findPostByIdAndAuthorId(postId, author.getId());
        verify(postRepository).delete(post);
    }

    @Test
    public void deletePost_invalidRequest_throwsPostNotFoundException() {
        Long postId = 1L;

        when(postRepository.findPostByIdAndAuthorId(postId, author.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> postService.deletePost(postId, author))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining(postId.toString());

        // verify that delete method is not called
        // do not use verifyNoInteractions unlike the previous unit tests because it requires a mock object.
        verify(postRepository, never()).delete(any());
    }

}
