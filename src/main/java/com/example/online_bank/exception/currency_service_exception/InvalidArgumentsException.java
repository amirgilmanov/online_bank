package com.example.online_bank.exception.currency_service_exception;

import com.example.online_bank.enums.CurrencyCode;
import lombok.experimental.StandardException;

@StandardException
public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException(String s, CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
    }
}
