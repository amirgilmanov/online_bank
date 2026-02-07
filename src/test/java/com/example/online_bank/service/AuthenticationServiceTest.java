package com.example.online_bank.service;

import com.example.online_bank.domain.dto.AuthenticationRequest;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.repository.UserRepository;
import com.example.online_bank.repository.VerifiedCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
    private AuthenticationRequest authRq;

    @BeforeEach
    void setUp() {
        authRq = new AuthenticationRequest(
                "testEmail@.com", "1234", "iphone 15", "chrome"
        );
    }
    @Test
    void failAuthenticationBy_EmailNotFound() {
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.signIn(authRq));
    }

    @Test
    void failAuthenticationBy_NotVerifiedCode() throws VerificationOtpException {

        User userMock = User.builder()
                .isVerified(false)
                .id(1L)
                .email("testEmail@.com")
                .build();

        when(userService.findByEmail(authRq.email())).thenReturn(Optional.of(userMock));

        doThrow(VerificationOtpException.class)
                .when(verifiedCodeService)
                .validateCode(userMock, authRq.code(), EMAIL);

        doThrow(VerificationOtpException.class)
                .when(userService)
                .verifyEmailCode(userMock, authRq.code());

        UserContainer userContainer = new UserContainer("random", "test", List.of("ROLE_USER"));

        when(tokenService.getAccessToken(userContainer)).thenReturn("accessToken");
        when(tokenService.getRefreshToken(userContainer)).thenReturn("refreshToken");
        when(tokenService.getIdToken(userContainer)).thenReturn("idToken");

        assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.signIn(authRq)
        );
        assertFalse(userMock.getIsVerified());
    }

    @Test
    @DisplayName("Ошибка верификации по почте: почта уже подтверждена")
    void failVerifyEmailCode_EmailAlreadyVerified() {

        Long userId = 1L;
        User userMock = User.builder().id(userId).isVerified(true).build();
        when(userService.findByEmail(authRq.email())).thenReturn(Optional.of(userMock));

        assertThrows(
                EntityAlreadyVerifiedException.class,
                () -> authenticationService.signIn(authRq));
    }
}