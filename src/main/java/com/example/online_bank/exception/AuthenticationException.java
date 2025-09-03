package com.example.online_bank.exception;

import lombok.experimental.StandardException;

@StandardException
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Неверный пин-код");
    }
}
