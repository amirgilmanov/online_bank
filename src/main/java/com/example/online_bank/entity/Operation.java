package com.example.online_bank.entity;

import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Сущность операция - уникальный идентификатор, дата+время (равна времени создания операции)
 */
@Builder
@Data
@AllArgsConstructor
public class Operation {
    private final UUID id;
    private LocalDateTime createdAt;
    private String accountId;
    private OperationType operationType;
    private BigDecimal amountMoney;
    private String description;
    private CurrencyCode currencyCode;
}
