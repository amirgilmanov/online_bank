package com.example.online_bank.service;

import com.example.online_bank.domain.dto.AuthenticationRequest;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.repository.UserRepository;
import com.example.online_bank.repository.VerifiedCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @Mock
    UserService userService;
    @InjectMocks
    AuthenticationService authenticationService;
    @Mock
    VerifiedCodeService verifiedCodeService;
    @Mock
    UserMapper userMapper;
    @Mock
    TokenService tokenService;
    @Mock
    UserRepository userRepository;
    @Mock
    VerifiedCodeRepository verifiedCodeRepository;

    @Test
    void failAuthenticationBy_EmailNotFound() {
        AuthenticationRequest authRq = new AuthenticationRequest("testEmail@.com", "1234");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> authenticationService.signIn(authRq));
    }

    @Test
    void failAuthenticationBy_NotVerifiedCode() {
        AuthenticationRequest authRqDto = new AuthenticationRequest("testEmail@.com", "1234");
        User user = User.builder()
                .isVerified(false)
                .id(1L)
                .email("testEmail@.com")
                .build();
        Mockito.when(userService.findByEmail(authRqDto.email())).thenReturn(Optional.of(user));
        Mockito.when(userService.verifyEmailCode(1L, "testEmail@.com")).thenReturn(false);
        Assertions.assertThrows(BadCredentialsException.class, () -> authenticationService.signIn(authRqDto));
    }
}