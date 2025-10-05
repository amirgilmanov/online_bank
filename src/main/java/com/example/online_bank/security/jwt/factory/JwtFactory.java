package com.example.online_bank.security.jwt.factory;

import com.example.online_bank.domain.dto.UserDetails;
import com.example.online_bank.enums.TokenType;

/**
 * Этот класс выдает необходимый тип токена
 */
public interface JwtFactory {
    /**
     * Создает jwt токен на основе переданного типа.
     * У каждой фабрики вызывается метод supports() для того, чтобы узнать, поддерживается ли переданный тип токена.
     */
    String createJwt(TokenType tokenType, UserDetails userDetails);
}
