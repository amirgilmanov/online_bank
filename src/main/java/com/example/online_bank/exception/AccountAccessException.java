package com.example.online_bank.exception;

import lombok.experimental.StandardException;

@StandardException
public class AccountAccessException extends RuntimeException {

    public AccountAccessException(String accountNumber, String User) {
        super("Счет %s не принадлежит пользователю %s");
    }
}
