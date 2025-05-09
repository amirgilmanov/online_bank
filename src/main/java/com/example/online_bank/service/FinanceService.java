package com.example.online_bank.service;

import com.example.online_bank.dto.FinanceOperationDto;
import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.entity.Account;
import com.example.online_bank.enums.OperationType;
import com.example.online_bank.mapper.OperationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.online_bank.enums.OperationType.*;

@RequiredArgsConstructor
@Service
public class FinanceService {
    private final AccountService accountService;
    private final OperationService operationService;
    private final OperationMapper operationMapper;
    private final ValidateFinanceService validateFinanceService;

    /**
     * Делать платеж:
     * Проверяем что счет принадлежит пользователю: если счет не принадлежит пользователю выкидываем ошибку.
     * Производит списание со счета. Записывает операцию в историю
     *
     * @param token Токен пользователя
     * @param dto   Содержит информацию о номере счета, код валюты, описание, количестве денег
     * @return Возвращает информацию об операции списания со счета
     */
    @Transactional()
    public OperationDtoResponse withdrawMoney(String token, FinanceOperationDto dto, boolean isTransaction) {
        validateFinanceService.validateParameters(token, dto.accountNumber(), dto.currencyCode(), dto.amount());
        Account account = accountService.findByAccountNumber(dto.accountNumber());

        accountService.withdrawMoney(dto.accountNumber(), dto.amount());

        return operationMapper.toWithdrawOperationDto(operationService.createOperation(
                LocalDateTime.now(),
                setOperationType(isTransaction, TRANSACTION, WITHDRAW),
                dto.amount(),
                dto.description(),
                account,
                dto.currencyCode()));
    }

    /**
     * Делать зачисление: на вход - номер счета, сумма, описание.
     * Зачисляет на банковский счет деньги и записывает операцию в историю.
     *
     * @param token Токен пользователя
     * @param dto   Содержит информацию о номере счета, код валюты, описание, количестве денег
     * @return Возвращает информацию об операции пополнении счета
     */
    @Transactional()
    public OperationDtoResponse depositMoney(String token, FinanceOperationDto dto, boolean isDeposit) {
        validateFinanceService.
                validateParameters(token, dto.accountNumber(), dto.currencyCode(), dto.amount());

        //TODO: упростиь логику
        Account account = accountService.findByAccountNumber(dto.accountNumber());
        BigDecimal balanceBefore = accountService.getBalance(dto.accountNumber());

        accountService.depositMoney(dto.accountNumber(), dto.amount());
        return operationMapper.toDepositOperationDto(operationService.createOperation(
                LocalDateTime.now(),
                setOperationType(isDeposit, DEPOSIT, BUY_CURRENCY),
                dto.amount(),
                dto.description(),
                account,
                dto.currencyCode()
        ), balanceBefore);
    }

    private OperationType setOperationType(boolean condition, OperationType trueType, OperationType falseType) {
        return condition ? trueType : falseType;
    }
}

