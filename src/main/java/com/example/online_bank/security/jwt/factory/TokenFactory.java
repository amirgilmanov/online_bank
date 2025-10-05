package com.example.online_bank.security.jwt.factory;

import com.example.online_bank.domain.dto.UserDetails;
import com.example.online_bank.enums.TokenType;

public interface TokenFactory {
    String createToken(TokenType type, UserDetails userDetails);

    boolean supports(TokenType supported);
}
