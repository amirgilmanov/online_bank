package com.example.online_bank.dto;

import java.math.BigDecimal;

public record CurrencyConvertedDtoResponse(
        String originalCurrencyName,
        String convertCurrencyName,
        BigDecimal convertibleCurrencyRate,
        BigDecimal convertibleCurrencyAmount,
        BigDecimal originalCurrencyAmount
) {
}
