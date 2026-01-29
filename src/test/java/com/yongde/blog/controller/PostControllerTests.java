    package com.yongde.blog.controller;

    import com.yongde.blog.dto.request.CreatePostRequestDto;
    import com.yongde.blog.dto.response.PostResponseDto;
    import com.yongde.blog.service.impl.PostServiceImpl;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
    import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
    import org.springframework.test.context.bean.override.mockito.MockitoBean;
    import org.springframework.test.web.servlet.MockMvc;
    import tools.jackson.databind.ObjectMapper;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.Mockito.when;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        public void PostController_CreatePost_ReturnsCreated() throws Exception {

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
                    .andExpect(status().isCreated());

        }
    }
