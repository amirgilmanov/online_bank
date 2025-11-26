package com.example.online_bank.config;

import com.example.online_bank.security.jwt.service.SecretKeyManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class JwtConfigTest {
    @InjectMocks
    private JwtConfig jwtConfig;
    @Mock
    SecretKeyManager secretKeyManager;

    @Test
    @Disabled
    void initSecretKey() {
        assertDoesNotThrow(() -> jwtConfig.initSecretKey());
    }
}