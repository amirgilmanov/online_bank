package com.example.online_bank.security.jwt.factory;

import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.enums.TokenType;

public interface TokenFactory {
    String createToken(TokenType type, UserContainer userContainer);

    boolean supports(TokenType supported);
}
