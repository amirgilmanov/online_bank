package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * @param accountNumber Номер счета
 * @param amount        Количество денег
 * @param description   Описание
 * @param currencyCode  Код валюты
 */
@Builder
public record FinanceOperationDto(
        String accountNumber,
        BigDecimal amount,
        String description,
        CurrencyCode currencyCode
) {
}
