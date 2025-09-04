package com.example.online_bank.security.jwt.factory;

import com.example.online_bank.enums.TokenType;
import org.springframework.security.core.Authentication;

public interface TokenFactory {
    String createToken(TokenType type, Authentication auth);

    boolean supports(TokenType supported);
}
