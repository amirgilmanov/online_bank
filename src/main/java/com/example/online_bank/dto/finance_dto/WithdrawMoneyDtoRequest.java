package com.example.online_bank.dto.finance_dto;

import com.example.online_bank.enums.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
public record WithdrawMoneyDtoRequest(
        @Schema(description = "Количество денег к списанию", example = "100")
        BigDecimal amountMoney,
        @Schema(description = "Описание к операции", example = "Скинул за тетради")
        String description,
        @Schema(description = "Код валюты", example = "USD, EUR")
        CurrencyCode depositCurrencyCode
) {
}
