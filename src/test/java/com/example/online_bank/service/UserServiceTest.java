package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.model.CustomUserDetails;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Mock
    VerifiedCodeService verifiedCodeService;

    @Test
    void successLoadUserByUsername() {
        User user = User.builder().id(1L).name("Test").build();
        Mockito.when(userRepository.findByName("Test")).thenReturn(Optional.ofNullable(user));
        CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername("Test");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("Test", userDetails.getUsername());
    }

    @Test
    void failLoadUserByUsername() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("Test"));
    }

    @Test
    @DisplayName("Успешная верификация почты")
    void successVerifyEmailCode() {
        Long userId = 1L;
        String correctOtp = "1234";
        User userMock = User.builder()
                .id(userId)
                .isVerified(false)
                .build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        Mockito.when(verifiedCodeService.validateCode(userMock, correctOtp, EMAIL)).thenReturn(true);
        boolean isVerified = userService.verifyEmailCode(userId, correctOtp);
        Assertions.assertTrue(isVerified);
    }

    @Test
    @DisplayName("Ошибка верификации по почте: пользователь не найден")
    void failVerifyEmailCode_ByIdNotFound() {
        Long userId = 1L;
        String correctOtp = "1234";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.verifyEmailCode(userId, correctOtp));
    }

    @Test
    @DisplayName("Ошибка верификации по почте: почта уже подтверждена")
    void failVerifyEmailCode_EmailAlreadyVerified() {
        Long userId = 1L;
        String correctOtp = "1234";
        User userMock = User.builder()
                .id(userId)
                .isVerified(true)
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        Mockito.when(verifiedCodeService.validateCode(userMock, correctOtp, EMAIL)).thenThrow(EntityAlreadyVerifiedException.class);
        Assertions.assertThrows(EntityAlreadyVerifiedException.class, () -> userService.verifyEmailCode(userId, correctOtp));
    }

    @Test
    @DisplayName("Ошибка верификации по почте: код просрочен или передан неверный код")
    void failVerifyEmailCode_OtpExpired() {
        Long userId = 1L;
        String correctOtp = "1234";
        User userMock = User.builder()
                .id(userId)
                .isVerified(true)
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        Mockito.when(verifiedCodeService.validateCode(userMock, correctOtp, EMAIL)).thenReturn(false);
        Assertions.assertFalse(userService.verifyEmailCode(userId, correctOtp));
    }
}