    package com.yongde.blog.controller;

    import com.yongde.blog.dto.request.CreatePostRequestDto;
    import com.yongde.blog.dto.response.PostResponseDto;
    import com.yongde.blog.entity.User;
    import com.yongde.blog.enums.PostStatus;
    import com.yongde.blog.enums.Role;
    import com.yongde.blog.exception.ApiResponse;
    import com.yongde.blog.exception.PostNotFoundException;
    import com.yongde.blog.service.CustomUserDetailsService;
    import com.yongde.blog.service.JWTService;
    import com.yongde.blog.service.PostService;
    import org.assertj.core.api.Assertions;
    import org.assertj.core.api.InstanceOfAssertFactories;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Disabled;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
    import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.test.context.bean.override.mockito.MockitoBean;
    import org.springframework.test.util.ReflectionTestUtils;
    import org.springframework.test.web.servlet.assertj.MockMvcTester;
    import org.springframework.test.web.servlet.assertj.MvcTestResult;
    import tools.jackson.databind.ObjectMapper;

    import java.util.List;

    import static org.assertj.core.api.AssertionsForClassTypes.tuple;
    import static org.mockito.ArgumentMatchers.*;
    import static org.mockito.Mockito.*;

    //Some people consider this to be integration testing while some consider it to be unit testing, depending on the test cases.
    //Here we are testing specifically on our controller only so we consider this to be unit testing.
    //But here we are using test slice from Java Spring to sort of reduce the number of beans being set up in the application context.
    @WebMvcTest(PostController.class)
    @AutoConfigureMockMvc(addFilters = false) //disable all filter beans.
    @Disabled("TODO: Refactor after Tag entity is complete")
    public class PostControllerTest {

        // allow us to simulate HTTP requests without needing a real server.
        @Autowired
        private MockMvcTester mockMvcTester;

        // allow us to convert java objects to JSON and vice versa
        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PostService postService;

        @MockitoBean
        private JWTService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        private User author;

        @BeforeEach
        void setup() {
            author = new User("Yong De", "Tan", "yongde@gmail.com", "password");
            author.setRole(Role.USER);
            ReflectionTestUtils.setField(author, "id", 1L);
        }

        @Test
        public void createPost_validRequest_returns201() {

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                    "title",
                    "content",
                    null,
                    null,
                    PostStatus.PUBLIC
            );

            PostResponseDto postResponseDto = new PostResponseDto(
                    1L,
                    "title",
                    "content",
                    null,
                    null,
                    PostStatus.PUBLIC,
                    null,
                    null,
                    author.getId()
            );

            when(postService.createPost(
                    any(CreatePostRequestDto.class),
                    any()
            )).thenReturn(postResponseDto);

            MvcTestResult result = mockMvcTester
                    .post()
                    .uri("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequestDto))
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.CREATED)
                    .containsHeader("Location")
                    .bodyJson()
                    .convertTo(PostResponseDto.class)
                    .satisfies(response -> Assertions.assertThat(response)
                            .extracting(PostResponseDto::id, PostResponseDto::authorId)
                            .containsExactly(postResponseDto.id(), postResponseDto.authorId()));

        }

        @Test
        public void createPost_invalidFields_returns400() {
            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                    null,
                    null,
                    null,
                    null,
                    PostStatus.PUBLIC
            );

            MvcTestResult result = mockMvcTester.post()
                    .uri("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequestDto))
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.BAD_REQUEST)
                    .bodyJson()
                    .convertTo(ApiResponse.class)
                    .satisfies(response -> Assertions.assertThat(response.fieldErrors())
                            .containsKeys("title", "content"));

        }

        @Test
        public void getAllPublicPosts_NonEmptyList_returns200() {
            PostResponseDto postResponseDto = new PostResponseDto(1L, "title1", "content1",
                    null, null, PostStatus.PUBLIC, null, null, author.getId());

            when(postService.getAllPublicPosts()).thenReturn(List.of(postResponseDto));

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(InstanceOfAssertFactories.list(PostResponseDto.class))
                    .satisfies(responses -> Assertions.assertThat(responses).hasSize(1)
                            .extracting(
                                    PostResponseDto::title,
                                    PostResponseDto::content,
                                    PostResponseDto::postStatus,
                                    PostResponseDto::authorId)
                            .containsExactly(
                                    tuple("title1", "content1", PostStatus.PUBLIC, author.getId())));
        }

        @Test
        public void getAllPublicPosts_EmptyList_returns200() {

            when(postService.getAllPublicPosts()).thenReturn(List.of());

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .isLenientlyEqualTo("""
                                   []
                            """);

        }

        @Test
        public void getAllAuthoredPosts_NonEmptyList_returns200() {
            PostResponseDto postResponseDto = new PostResponseDto(1L, "title1", "content1",
                    null, null, PostStatus.PUBLIC, null, null, author.getId());

            when(postService.getAllAuthoredPosts(any())).thenReturn(List.of(postResponseDto));

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts/me")
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(InstanceOfAssertFactories.list(PostResponseDto.class))
                    .satisfies(responses ->
                        Assertions.assertThat(responses).hasSize(1)
                                .extracting(
                                        PostResponseDto::title,
                                        PostResponseDto::content,
                                        PostResponseDto::postStatus,
                                        PostResponseDto::authorId)
                                .containsExactly(
                                        tuple("title1", "content1", PostStatus.PUBLIC, author.getId()))
                    );
        }

        @Test
        public void getAllAuthoredPosts_EmptyList_returns200() {

            when(postService.getAllAuthoredPosts(any())).thenReturn(List.of());

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts/me")
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .isLenientlyEqualTo("""
                                   []
                            """);

        }

        @Test
        public void getPost_validRequest_returns200() {
            Long postId = 1L;
            PostResponseDto postResponseDto = new PostResponseDto(postId, "title1", "content1",
                    null, null, PostStatus.PUBLIC, null, null, author.getId());

            when(postService.getPost(anyLong(), any()))
                    .thenReturn(postResponseDto);

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(PostResponseDto.class)
                    .satisfies(response -> {
                        Assertions.assertThat(response.id()).isEqualTo(postId);
                        Assertions.assertThat(response.title()).isEqualTo(postResponseDto.title());
                        Assertions.assertThat(response.content()).isEqualTo(postResponseDto.content());
                        Assertions.assertThat(response.postStatus()).isEqualTo(PostStatus.PUBLIC);
                        Assertions.assertThat(response.authorId()).isEqualTo(author.getId());
                    });

        }

        @Test
        public void getPost_nonExistentPost_returns404() {
            Long postId = 1L;

            when(postService.getPost(anyLong(), any()))
                    .thenThrow(new PostNotFoundException(postId));

            MvcTestResult result = mockMvcTester.get()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.NOT_FOUND)
                    .bodyJson()
                    .convertTo(ApiResponse.class)
                    .satisfies(response ->
                        Assertions.assertThat(response.fieldErrors().containsKey("post")).isNotNull()
                    );

        }


        @Test
        public void updatePost_validRequest_returns200() {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto("title", "content",
                    null, null, PostStatus.PUBLIC);

            PostResponseDto postResponseDto = new PostResponseDto(
                    postId,
                    "updated_title",
                    "updated_content",
                    null,
                    null,
                    PostStatus.PUBLIC,
                    null,
                    null,
                    author.getId()
            );

            when(postService.updatePost(eq(postId), any(CreatePostRequestDto.class), any()))
                    .thenReturn(postResponseDto);

            MvcTestResult result = mockMvcTester.put()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequestDto))
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(PostResponseDto.class)
                    .satisfies(response -> {
                        Assertions.assertThat(response.id()).isEqualTo(postId);
                        Assertions.assertThat(response.title()).isEqualTo(postResponseDto.title());
                        Assertions.assertThat(response.content()).isEqualTo(postResponseDto.content());
                        Assertions.assertThat(response.postStatus()).isEqualTo(postResponseDto.postStatus());
                        Assertions.assertThat(response.authorId()).isEqualTo(author.getId());
                    });
        }

        @Test
        public void updatePost_invalidFields_returns400() {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(null, null, null,
                    null, null);

            MvcTestResult result = mockMvcTester.put()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequestDto))
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.BAD_REQUEST)
                    .bodyJson()
                    .convertTo(ApiResponse.class)
                    .satisfies(response -> {
                        Assertions.assertThat(response.timestamp()).isNotNull();
                        Assertions.assertThat(response.fieldErrors())
                                .containsKeys("title", "content", "status");
                    });
        }

        @Test
        public void updatePost_nonExistentPost_returns404() {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto("title", "post",
                    null, null, PostStatus.PUBLIC);

            //must use eq() here so that eq(postId) turns into a matcher. this is required because any() is a matcher and postId is a concrete value
            when(postService.updatePost(eq(postId), any(CreatePostRequestDto.class), any()))
                    .thenThrow(new PostNotFoundException(postId));


            MvcTestResult result = mockMvcTester.put()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPostRequestDto))
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.NOT_FOUND)
                    .bodyJson()
                    .convertTo(ApiResponse.class)
                    .satisfies(response -> {
                        Assertions.assertThat(response.timestamp()).isNotNull();
                        Assertions.assertThat(response.fieldErrors())
                                .containsKeys("post");
                    });
        }

        @Test
        public void deletePost_existingPost_returns204() {
            Long postId = 1L;

            MvcTestResult result = mockMvcTester.delete()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.NO_CONTENT);

            verify(postService).deletePost(eq(postId), any());
        }

        @Test
        public void deletePost_notExistentPost_returns404() {
            Long postId = 1L;

            //we must do it this way because postService.deletePost is a void method so we cannot return anything.
            doThrow(new PostNotFoundException(postId))
                    .when(postService)
                    .deletePost(eq(postId), any());

            MvcTestResult result = mockMvcTester.delete()
                    .uri("/api/v1/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange();

            Assertions.assertThat(result)
                    .hasStatus(HttpStatus.NOT_FOUND)
                    .bodyJson()
                    .convertTo(ApiResponse.class)
                    .satisfies(response -> {
                        Assertions.assertThat(response.timestamp()).isNotNull();
                        Assertions.assertThat(response.fieldErrors().containsKey("post")).isNotNull();
                    });
            verify(postService).deletePost(eq(postId), any());
        }
    }
