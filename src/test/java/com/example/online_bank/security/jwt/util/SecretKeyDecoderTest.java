package com.example.online_bank.security.jwt.util;

import com.example.online_bank.security.jwt.service.SecretKeyManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

@SpringBootTest
@RequiredArgsConstructor
class SecretKeyDecoderTest {
    private final SecretKeyManager secretKeyManager;

    @Test
    void decode() {
        SecretKey secretKey = secretKeyManager.createSecretKey();
        secretKeyManager.encodeAndWriteKey();
    }
}