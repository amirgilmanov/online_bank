package com.example.online_bank.exception.currency_service_exception;

import lombok.experimental.StandardException;

@StandardException
public class CurrencyPairsNotFoundException extends RuntimeException {
    public CurrencyPairsNotFoundException(String s, String currencyPair, String reversedCurrencyPair) {
    }
}
