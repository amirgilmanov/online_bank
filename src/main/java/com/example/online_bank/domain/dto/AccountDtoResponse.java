package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;

import java.math.BigDecimal;

/**
 * @param accountNumber номер счета
 * @param currencyCode код валюты
 * @param balance баланс
 * @param holderName имя пользователя
 * @param holderSurname фамилия пользователя
 */
public record AccountDtoResponse(
        String accountNumber,
        CurrencyCode currencyCode,
        BigDecimal balance,
        String holderName,
        String holderSurname
) {
}

