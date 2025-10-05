package com.example.online_bank.service;

import com.example.online_bank.domain.dto.UserDetails;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.security.jwt.factory.impl.JwtFactoryOrchestrator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final UserService userService;
    private final JwtFactoryOrchestrator jwtFactoryOrchestrator;

    @Transactional
    @Deprecated
    public String createAndSaveToken(User user) {
        log.info("Creating new token for user: {}", user);
        String token = "online%stoken".formatted(UUID.randomUUID());
        // user.setToken(token);
        userService.save(user);
        return token;
    }

    public String getAccessToken(UserDetails userDetails) {
        return jwtFactoryOrchestrator.createJwt(TokenType.ACCESS, userDetails);
    }

    public String getIdToken(UserDetails userDetails) {
        return jwtFactoryOrchestrator.createJwt(TokenType.ID, userDetails);
    }

    public String getRefreshToken(UserDetails userDetails) {
        return jwtFactoryOrchestrator.createJwt(TokenType.REFRESH, userDetails);
    }


}
