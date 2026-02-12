package com.yongde.blog.service;

import com.yongde.blog.dto.request.LoginRequestDto;
import com.yongde.blog.dto.response.AuthResponseDto;
import com.yongde.blog.entity.User;
import com.yongde.blog.entity.UserPrincipal;
import com.yongde.blog.enums.Role;
import com.yongde.blog.service.impl.AuthServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void verifyAccount_validCredentials_returnsAuthResponseDto() {

        //Arrange
        User user = new User("Yong De", "Tan", "yongdetan@gmail.com", "password");
        user.setRole(Role.USER);

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "yongdetan@gmail.com",
                "password"
        );

        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());
        String token = "test-secret-key-that-is-at-least-256-bits-long-for-testing-purposes";

        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtService.generateToken(userPrincipal.getId())).thenReturn(token);
        when(jwtService.getExpirationMs()).thenReturn(3600000L);

        //Act
        AuthResponseDto result = authService.verifyAccount(loginRequestDto);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.token()).isEqualTo(token);
        Assertions.assertThat(result.type()).isEqualTo("Bearer");
        Assertions.assertThat(result.expiresIn()).isEqualTo(3600000L);
        Assertions.assertThat(result.userId()).isEqualTo(userPrincipal.getId());

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());

        UsernamePasswordAuthenticationToken captured = captor.getValue();
        Assertions.assertThat(captured.getPrincipal()).isEqualTo(loginRequestDto.email());
        Assertions.assertThat(captured.getCredentials()).isEqualTo(loginRequestDto.password());

    }

}

