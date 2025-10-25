package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;

import java.math.BigDecimal;

public record AccountDtoResponse(
        String accountNumber,
        CurrencyCode currencyCode,
        BigDecimal balance,
        String holderName,
        String holderSurname
) {
}

