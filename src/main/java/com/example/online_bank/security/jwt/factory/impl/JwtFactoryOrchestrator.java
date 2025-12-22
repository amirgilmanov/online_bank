package com.example.online_bank.security.jwt.factory.impl;

import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.exception.UnsupportedTokenTypeException;
import com.example.online_bank.security.jwt.factory.JwtFactory;
import com.example.online_bank.security.jwt.factory.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFactoryOrchestrator implements JwtFactory {

    private final Set<TokenFactory> tokenFactories;

    @Override
    public String createJwt(TokenType tokenType, UserContainer userContainer) {
        log.debug("Create JWT");
        return tokenFactories.stream()
                .filter(tokenFactory -> tokenFactory.supports(tokenType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedTokenTypeException(tokenType))
                .createToken(tokenType, userContainer);
    }
}
