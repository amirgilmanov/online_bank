package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.security.jwt.factory.JwtFactoryOrchestrator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final UserService userService;
    private JwtFactoryOrchestrator jwtFactoryOrchestrator;

    @Transactional
    @Deprecated
    public String createAndSaveToken(User user) {
        log.info("Creating new token for user: {}", user);
        String token = "online%stoken".formatted(UUID.randomUUID());
        // user.setToken(token);
        userService.save(user);
        return token;
    }

    public String getAccessToken(Authentication token) {
       return jwtFactoryOrchestrator.createJwt(TokenType.ACCESS, token);
    }

    public String getIdToken(Authentication token) {
        return jwtFactoryOrchestrator.createJwt(TokenType.ID, token);
    }

    public String getRefreshToken(Authentication token) {
        return jwtFactoryOrchestrator.createJwt(TokenType.REFRESH, token);
    }


}
