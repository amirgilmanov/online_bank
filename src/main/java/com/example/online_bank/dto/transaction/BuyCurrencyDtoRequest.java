package com.example.online_bank.dto.transaction;

import com.example.online_bank.enums.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BuyCurrencyDtoRequest(
        @Schema(description = "Номер счёта с которого произойдет списание", example = "100")
        String baseAccountId,
        @Schema(description = "Номер счёта с которого произойдет списание", example = "100")
        String targetAccountId,
        @Schema(description = "Количество денег к переводу", example = "100")
        BigDecimal amountMoney,
        @Schema(description = "Код валюты", example = "EUR, CNY")
        CurrencyCode currencyCode
) {
}
