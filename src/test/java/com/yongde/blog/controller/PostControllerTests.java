    package com.yongde.blog.controller;

    import com.yongde.blog.dto.request.CreatePostRequestDto;
    import com.yongde.blog.dto.response.PostResponseDto;
    import com.yongde.blog.exception.PostNotFoundException;
    import com.yongde.blog.service.impl.PostServiceImpl;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
    import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
    import org.springframework.test.context.bean.override.mockito.MockitoBean;
    import org.springframework.test.web.servlet.MockMvc;
    import tools.jackson.databind.ObjectMapper;

    import java.util.List;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.eq;
    import static org.mockito.Mockito.doThrow;
    import static org.mockito.Mockito.when;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    //Technically, this is considered integration testing (similar to PostRepositoryTests) but some websites do call it unit testing.
    //But here we are using test slice from Java Spring to sort of reduce the number of beans being set up in the application context.
    @WebMvcTest(PostController.class) //use this when we are testing the web layer so that we only initialize context for web layer
    @AutoConfigureMockMvc(addFilters = false) //disable all filter beans.
    public class PostControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PostServiceImpl postService;

        @Test
        public void PostController_CreatePost_Returns201() throws Exception {

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                    "title",
                    "content",
                    null,
                    null
            );

            PostResponseDto postResponseDto = new PostResponseDto(
                    1L,
                    "title",
                    "content",
                    null,
                    null,
                    null,
                    null
            );

            when(postService.createPost(any(CreatePostRequestDto.class))).thenReturn(postResponseDto);

            mockMvc.perform(post("/api/v1/posts")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createPostRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location")) //check that we indeed included the location of the newly created post
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        public void PostController_CreatePost_Returns400() throws Exception {
            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(
                    null,
                    null,
                    null,
                    null
            );

            mockMvc.perform(post("/api/v1/posts")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createPostRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.fieldErrors.title").exists())
                    .andExpect(jsonPath("$.fieldErrors.content").exists());
        }

        @Test
        public void PostController_GetAllPosts_EmptyList_Returns200() throws Exception {

            when(postService.getAllPosts()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/posts")
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]")); //checks the entire response body is only []
        }

        @Test
        public void PostController_GetAllPosts_NonEmptyList_Returns200() throws Exception {
            PostResponseDto postResponseDto1 = new PostResponseDto(1L, "title1", "content1", null, null, null, null);
            PostResponseDto postResponseDto2 = new PostResponseDto(2L, "title2", "content2", null, null, null, null);

            when(postService.getAllPosts()).thenReturn(List.of(postResponseDto1, postResponseDto2));

            mockMvc.perform(get("/api/v1/posts")
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].title").value("title1"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].title").value("title2"));
        }

        @Test
        public void PostController_GetPost_Returns200() throws Exception {
            Long postId = 1L;
            PostResponseDto postResponseDto = new PostResponseDto(postId, "title1", "content1", null, null, null, null);

            when(postService.getPost(postId)).thenReturn(postResponseDto);

            mockMvc.perform(get("/api/v1/posts/{postId}", postId)
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(postId))
                    .andExpect(jsonPath("$.title").value("title1"))
                    .andExpect(jsonPath("$.content").value("content1"));
        }

        @Test
        public void PostController_GetPost_Returns404() throws Exception {
            Long postId = 1L;

            when(postService.getPost(postId)).thenThrow(new PostNotFoundException(postId));

            mockMvc.perform(get("/api/v1/posts/{postId}", postId)
                    .contentType("application/json"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.fieldErrors.post").exists());

        }

        @Test
        public void PostController_UpdatePost_ValidInput_Returns200() throws Exception {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto("title", "content", null, null);

            PostResponseDto postResponseDto = new PostResponseDto(
                    postId,
                    "updated_title",
                    "updated_content",
                    null,
                    null,
                    null,
                    null
            );

            when(postService.updatePost(postId, createPostRequestDto)).thenReturn(postResponseDto);

            mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(createPostRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(postId))
                    .andExpect(jsonPath("$.title").value("updated_title"))
                    .andExpect(jsonPath("$.content").value("updated_content"));

        }

        @Test
        public void PostController_UpdatePost_InvalidInput_Returns400() throws Exception {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto(null, null, null, null);

            mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(createPostRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.fieldErrors.title").exists())
                    .andExpect(jsonPath("$.fieldErrors.content").exists());
        }

        @Test
        public void PostController_UpdatePost_PostNotExist_Returns404() throws Exception {
            Long postId = 1L;

            CreatePostRequestDto createPostRequestDto = new CreatePostRequestDto("title", "post", null, null);

            //must use eq() here so that eq(postId) turns into a matcher. this is required because any() is a matcher and postId is a concrete value
            when(postService.updatePost(eq(postId), any(CreatePostRequestDto.class))).thenThrow(new PostNotFoundException(postId));

            mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(createPostRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.fieldErrors.post").exists());
        }

        @Test
        public void PostController_DeletePost_Returns204() throws Exception {
            Long postId = 1L;

            mockMvc.perform(delete("/api/v1/posts/{postId}", postId)
                    .contentType("application/json"))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void PostController_DeletePost_PostNotExist_Returns404() throws Exception {
            Long postId = 1L;

            //we must do it this way because postService.deletePost is a void method so we cannot return anything.
            doThrow(new PostNotFoundException(postId))
                    .when(postService)
                    .deletePost(postId);

            mockMvc.perform(delete("/api/v1/posts/{postId}", postId)
                            .contentType("application/json"))
                            .andExpect(status().isNotFound())
                            .andExpect(jsonPath("$.fieldErrors.post").exists());
        }
    }
