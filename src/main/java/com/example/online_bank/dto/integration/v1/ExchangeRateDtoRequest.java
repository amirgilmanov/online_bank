package com.example.online_bank.dto.integration.v1;

import com.example.online_bank.enums.CurrencyCode;

import java.math.BigDecimal;

public record ExchangeRateDtoRequest(
        CurrencyCode baseCurrency,
        CurrencyCode targetCurrency,
        BigDecimal targetRate
) {
}
