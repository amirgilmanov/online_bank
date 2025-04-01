package com.example.online_bank.dto;

import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO операции для http ответа
 *
 * @param id            Id операции
 * @param createdAt     Время создания
 * @param accountNumber Номер счета
 * @param operationType Тип операции
 * @param amount        Количество денег
 * @param description   Описание
 * @param currencyCode  Код валюты
 */
public record OperationInfoDtoResponse(
        Long id,
        LocalDateTime createdAt,
        String accountNumber,
        OperationType operationType,
        BigDecimal amount,
        String description,
        CurrencyCode currencyCode
) {
}
