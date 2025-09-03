package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;
import lombok.NonNull;

import java.math.BigDecimal;

public record AccountDtoResponse(
        String accountNumber,
        CurrencyCode currencyCode,
        BigDecimal balance,
        @NonNull String holderName,
        String holderSurname
) {
}

