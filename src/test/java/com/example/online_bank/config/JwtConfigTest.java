package com.example.online_bank.config;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class JwtConfigTest {
    private final JwtConfig jwtConfig;

    @Test
    @Disabled
    void initSecretKey() {
        Assertions.assertDoesNotThrow(jwtConfig::initSecretKey);
    }
}