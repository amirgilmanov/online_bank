package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;

import java.math.BigDecimal;

public record ConvertCurrencyResponse(
        CurrencyCode targetCurrency,
        BigDecimal convertedRate,
        BigDecimal amount,
        CurrencyCode baseCurrency) {
}
