package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreateUserRequestDto;
import com.yongde.blog.dto.response.UserResponseDto;
import com.yongde.blog.entity.User;
import com.yongde.blog.exception.EmailExistsException;
import com.yongde.blog.mapper.UserMapper;
import com.yongde.blog.repository.UserRepository;
import com.yongde.blog.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUser_validRequest_returnsUserResponseDto () {

        // Arrange (setting up the mock data for testing)
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                "password"
        );

        User user = new User(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password" //password before hash
        );

        User savedUser = new User(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "hashed" //password after hash
        );

        UserResponseDto expectedResponse = new UserResponseDto(
                1L,
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                null,
                null
        );

        when(userRepository.findByEmail(createUserRequestDto.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(createUserRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(createUserRequestDto.password())).thenReturn("hashed");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedResponse);

        // Act

        UserResponseDto result = userService.createUser(createUserRequestDto);

        // Assert

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        Assertions.assertThat(capturedUser.getPasswordHash()).isEqualTo("hashed");

        verify(userRepository).findByEmail(createUserRequestDto.email());
        verify(userMapper).toEntity(createUserRequestDto);
        verify(passwordEncoder).encode("password");
        verify(userMapper).toDto(savedUser);
    }

    @Test
    public void createUser_existingEmail_throwsEmailExistsException() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                "Yong De",
                "Tan",
                "yongdetan@gmail.com",
                "password",
                "password"
        );

        when(userRepository.findByEmail(createUserRequestDto.email()))
                .thenThrow(new EmailExistsException(createUserRequestDto.email()));

        Assertions.assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(EmailExistsException.class)
                .hasMessageContaining(createUserRequestDto.email());

        verify(userRepository).findByEmail(createUserRequestDto.email());
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(User.class));
    }

}
