package com.example.online_bank.dto.finance_dto;

import com.example.online_bank.enums.CurrencyCode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @param amountMoney количество денег
 * @param description описание
 * @param depositCurrencyCode Код валюты при зачислении
 */
public record DepositMoneyDtoRequest(
        @Schema(description = "Количество денег к пополнению", example = "100")
        BigDecimal amountMoney,
        @Schema(description = "Описание к операции", example = "Пополнение в банкомате")
        String description,
        @Schema(description = "Код валюты", example = "USD, EUR")
        CurrencyCode depositCurrencyCode
) {
}
