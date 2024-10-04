package com.example.online_bank.dto;

import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Данное dto используется для получения ответа от сервера после совершения операций
 * @param operationId id операции
 * @param operationType тип операции
 * @param accountId id счета
 * @param amountMoney количество денег
 * @param description описание операции
 * @param localDateTime время операции
 * @param paymentCurrencyCode код валюты
 */
public record OperationDtoResponse(
        UUID operationId,
        OperationType operationType,
        String accountId,
        BigDecimal amountMoney,
        String description,
        LocalDateTime localDateTime,
        CurrencyCode paymentCurrencyCode
) {
}
