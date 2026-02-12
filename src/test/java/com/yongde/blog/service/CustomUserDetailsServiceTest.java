package com.yongde.blog.service;

import com.yongde.blog.entity.User;
import com.yongde.blog.entity.UserPrincipal;
import com.yongde.blog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void loadUserByUsername_validEmail_returnsUserDetails() {
        // In this project, we are using email as our username.

        String email = "yongdetan@gmail.com";
        User user = new User(
                "Yong De",
                "Tan",
                email,
                "password"
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUsername()).isEqualTo(email);

        verify(userRepository).findByEmail(email);

    }

    @Test
    public void loadUserByUsername_invalidEmail_throwsUsernameNotFoundException() {
        // In this project, we are using email as our username.

        String email = "yongdetan@gmail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(email);

    }

    @Test
    public void loadUserById_validId_returnsUserDetails() {

        Long id = 1L;

        String email = "yongdetan@gmail.com";
        User user = new User(
                "Yong De",
                "Tan",
                email,
                "password"
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserById(id);

        Assertions.assertThat(result).isNotNull();

        UserPrincipal userPrincipal = (UserPrincipal) result;

        //we use email to assert since we cannot set id and userRepository is mocked and email is unique
        Assertions.assertThat(userPrincipal.getUsername()).isEqualTo("yongdetan@gmail.com");

        verify(userRepository).findById(id);
    }

    @Test
    public void loadUserById_invalidId_throwsUsernameNotFoundException() {

        Long id = 1L;

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> customUserDetailsService.loadUserById(id))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

    }
}
