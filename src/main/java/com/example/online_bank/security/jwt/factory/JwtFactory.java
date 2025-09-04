package com.example.online_bank.security.jwt.factory;

import com.example.online_bank.enums.TokenType;
import org.springframework.security.core.Authentication;

/**
 * Этот класс выдает необходимый тип токена
 */
public interface JwtFactory {
    String createJwt(TokenType tokenType, Authentication authentication);
}
