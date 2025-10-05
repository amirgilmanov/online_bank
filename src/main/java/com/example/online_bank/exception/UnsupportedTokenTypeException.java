package com.example.online_bank.exception;

import com.example.online_bank.enums.TokenType;
import lombok.experimental.StandardException;

@StandardException
public class UnsupportedTokenTypeException extends RuntimeException {
    public UnsupportedTokenTypeException(TokenType tokenType) {
        super("Token type %s is not supported".formatted(tokenType));
    }
}
