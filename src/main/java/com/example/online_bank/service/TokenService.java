package com.example.online_bank.service;

import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.security.jwt.factory.impl.JwtFactoryOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final UserService userService;
    private final JwtFactoryOrchestrator jwtFactoryOrchestrator;

    public String getAccessToken(UserContainer userContainer) {
        return jwtFactoryOrchestrator.createJwt(TokenType.ACCESS, userContainer);
    }

    public String getIdToken(UserContainer userContainer) {
        return jwtFactoryOrchestrator.createJwt(TokenType.ID, userContainer);
    }

    public String getRefreshToken(UserContainer userContainer) {
        return jwtFactoryOrchestrator.createJwt(TokenType.REFRESH, userContainer);
    }
}