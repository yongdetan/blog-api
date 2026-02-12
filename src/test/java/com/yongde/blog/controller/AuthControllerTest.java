package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.dto.response.AuthResponseDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.enums.Role;
import com.yongde.blog.exception.ApiResponse;
import com.yongde.blog.service.AuthService;
import com.yongde.blog.service.CustomUserDetailsService;
import com.yongde.blog.service.JWTService;
import com.yongde.blog.service.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    public void createUser_validRequest_returns201() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                "password"
        );

        UserResponseDto userResponseDto = new UserResponseDto(
                1L,
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                Role.USER,
                null,
                null
        );

        when(userService.createUser(any(CreateUserRequestDto.class)))
                .thenReturn(userResponseDto);

        MvcTestResult results = mockMvcTester.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto))
                .exchange();

        Assertions.assertThat(results)
                .hasStatus(HttpStatus.CREATED)
                .containsHeader("Location")
                .bodyJson()
                .convertTo(UserResponseDto.class)
                .isEqualTo(userResponseDto);
    }

    @Test
    public void createUser_validRequest_returns400() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                null,
                null,
                null,
                null,
                null
        );

        MvcTestResult results = mockMvcTester.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto))
                .exchange();

        Assertions.assertThat(results)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .convertTo(ApiResponse.class)
                .satisfies(response -> Assertions.assertThat(response.fieldErrors())
                        .containsKeys("firstName", "lastName", "email", "password")
                );
    }

    @Test
    public void login_validRequest_returns200() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("yongdetan@gmail.com", "password");

        AuthResponseDto authResponseDto = new AuthResponseDto(
                "token",
                "Bearer",
                12345L,
                1L
        );

        when(authService.verifyAccount(any(LoginRequestDto.class)))
                .thenReturn(authResponseDto);

        MvcTestResult results = mockMvcTester.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))
                .exchange();

        Assertions.assertThat(results)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(AuthResponseDto.class)
                .isEqualTo(authResponseDto);

    }

    @Test
    public void login_invalidRequest_returns400() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, null);

        MvcTestResult results = mockMvcTester.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto))
                .exchange();

        Assertions.assertThat(results)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .convertTo(ApiResponse.class)
                .satisfies(response -> Assertions.assertThat(response.fieldErrors())
                        .containsKeys("email", "password")
                );

    }

}
