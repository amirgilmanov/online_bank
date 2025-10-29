package com.example.online_bank.config;

import com.example.online_bank.security.jwt.service.SecretKeyManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class JwtConfigTest {
    private final SecretKeyManager secretKeyManager;
    private final String testSecretFileName = "test_secret";

    @Test
    void initSecretKey() {

    }
}