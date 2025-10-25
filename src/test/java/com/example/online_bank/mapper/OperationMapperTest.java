package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.OperationInfoDto;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.Operation;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
class OperationMapperTest {
    private final OperationMapper operationMapper = new OperationMapperImpl();

    @Test
    void toWithdrawOperationDto() {
        //arrange(подготовка данных)
        Operation operation = Operation.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .operationType(OperationType.WITHDRAW)
                .amount(new BigDecimal("100"))
                .description("test")
                .currencyCode(CurrencyCode.RUB)
                .build();

        Account account = Account.builder()
                .accountNumber("0000001111100000")
                .balance(BigDecimal.valueOf(1000))
                .currencyCode(CurrencyCode.RUB)
                .isBlocked(false)
                .operations(List.of(operation))
                .build();

        operation.setAccount(account);

        //Act вызываем метод который мы тестировали
        OperationDtoResponse withdrawOperationDto = operationMapper.toWithdrawOperationDto(operation);
        log.info(withdrawOperationDto.toString());
        Assertions.assertNotNull(withdrawOperationDto);
        assertEquals(BigDecimal.valueOf(900), withdrawOperationDto.amountAfter());
        assertEquals(BigDecimal.valueOf(1000), withdrawOperationDto.amountBefore());
    }

    @Test
    void toDepositOperationDto() {
        //arrange(подготовка данных)
        Operation operation = Operation.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100"))
                .description("test")
                .currencyCode(CurrencyCode.RUB)
                .build();

        Account account = Account.builder()
                .accountNumber("0000001111100000")
                .balance(BigDecimal.valueOf(1000))
                .currencyCode(CurrencyCode.RUB)
                .isBlocked(false)
                .operations(List.of(operation))
                .build();

        operation.setAccount(account);

        //Act вызываем метод который мы тестировали
        OperationDtoResponse depositOperationDto = operationMapper.toDepositOperationDto(operation);
        log.info(depositOperationDto.toString());
        Assertions.assertNotNull(depositOperationDto);
        assertEquals(BigDecimal.valueOf(1100), depositOperationDto.amountAfter());
        assertEquals(BigDecimal.valueOf(1000), depositOperationDto.amountBefore());
    }

    @Test
    void toOperationInfoDto() {
        //arrange(подготовка данных)
        LocalDateTime createdAt = LocalDateTime.of(2025, 10, 22, 10, 55);
        Operation operation = Operation.builder()
                .id(1L)
                .createdAt(createdAt)
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100"))
                .description("test")
                .currencyCode(CurrencyCode.RUB)
                .build();

        Account account = Account.builder()
                .accountNumber("0000001111100000")
                .balance(BigDecimal.valueOf(1000))
                .currencyCode(CurrencyCode.RUB)
                .isBlocked(false)
                .operations(List.of(operation))
                .build();

        operation.setAccount(account);

        //act
        OperationInfoDto operationInfoDto = operationMapper.toOperationInfoDto(operation);
        log.info(operationInfoDto.toString());

        Assertions.assertNotNull(operationInfoDto);
        assertEquals("0000001111100000", operationInfoDto.accountNumber());
        assertEquals(1L, operationInfoDto.id());
        assertEquals(createdAt, operationInfoDto.createdAt());
        assertEquals(OperationType.DEPOSIT, operationInfoDto.operationType());
        assertEquals(BigDecimal.valueOf(100), operationInfoDto.amount());
        assertEquals("test", operationInfoDto.description());
        assertEquals(CurrencyCode.RUB, operationInfoDto.currencyCode());
    }

//return (operation.getAccount().getBalance().subtract(operation.getAmount()));
    @Test
    void calcAmountAfterWithdraw() {
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation operation = Operation.builder().amount(amount).account(Account.builder().balance(BigDecimal.valueOf(1000)).build()).build();
        BigDecimal amountAfterWithdraw = operationMapper.calcAmountAfterWithdraw(operation);
        assertEquals(amountAfterWithdraw, BigDecimal.valueOf(900));
    }

    @Test
    void calcAmountBeforeDeposit() {
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation operation = Operation.builder().amount(amount).account(Account.builder().balance(BigDecimal.valueOf(1000)).build()).build();
        BigDecimal amountAfterWithdraw = operationMapper.calcAmountAfterDeposit(operation);
        assertEquals(amountAfterWithdraw, BigDecimal.valueOf(1100));
    }
}