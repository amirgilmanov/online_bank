package com.example.online_bank.config;

import com.example.online_bank.security.jwt.service.impl.SecretKeyManagerImpl;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Getter
public class JwtConfig {
    @Value("${jwt.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${jwt.refresh-token-life-time}")
    private Duration refreshAndIdTokenLifetime;

    @Value("${jwt.not-before}")
    private Duration notBeforeTime;

    @Value("${jwt.secret-file-name}")
    private String fileName;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.issuer}")
    private String issuer;

    private SecretKey key;

    private final SecretKeyManagerImpl secretKeyManager;

    @PostConstruct
    public void initSecretKey() throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            this.key = secretKeyManager.readAndDecodeKey(fileName);
        } else {
            SecretKey secretKey = secretKeyManager.createSecretKey();
            this.key = secretKey;
            secretKeyManager.encodeAndWriteKey(new FileWriter(fileName), secretKey);
        }
    }
}
