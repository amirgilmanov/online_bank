package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.model.CustomUserDetails;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private VerifiedCodeService verifiedCodeService;

    @Test
    void successLoadUserByUsername() {
        User user = User.builder().id(1L).name("Test").build();
        when(userRepository.findByName("Test")).thenReturn(Optional.ofNullable(user));
        CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername("Test");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("Test", userDetails.getUsername());
    }

    @Test
    void failLoadUserByUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("Test"));
    }

    @Test
    @DisplayName("Успешная верификация почты")
    void successVerifyEmailCode() throws VerificationOtpException {
        Long userId = 1L;
        String correctOtp = "1234";
        User userMock = User.builder()
                .id(userId)
                .isVerified(false)
                .build();

//        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        doNothing().when(verifiedCodeService).validateCode(userMock, correctOtp, EMAIL);

        assertDoesNotThrow(() -> userService.verifyEmailCode(userMock, correctOtp));
    }

    @Test
    @DisplayName("Ошибка верификации по почте: код просрочен или передан неверный код")
    void failVerifyEmailCode_OtpExpired() throws VerificationOtpException {
        String correctOtp = "1234";
        User userMock = User.builder()
                .isVerified(false)
                .build();

        doThrow(VerificationOtpException.class)
                .when(verifiedCodeService)
                .validateCode(userMock, correctOtp, EMAIL);

        assertThrows(
                VerificationOtpException.class,
                () -> userService.verifyEmailCode(userMock, correctOtp));
        assertFalse(userMock.getIsVerified());
    }
}