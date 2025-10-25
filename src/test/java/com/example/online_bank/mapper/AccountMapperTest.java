package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.AccountDtoResponse;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.Operation;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class AccountMapperTest {
    private final AccountMapper accountMapper = new AccountMapperImpl();

    @Test
    void successMapToAccountDtoResponse() {
        //подготовка данных arrange
        User user = User.builder()
                .name("test")
                .uuid(UUID.randomUUID())
                .surname("test")
                .patronymic("test")
                .id(1L)
                .build();
        Operation operation = Operation.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .operationType(OperationType.DEPOSIT)
                .amount(new BigDecimal("100"))
                .description("test")
                .currencyCode(CurrencyCode.RUB)
                .build();

        Account account = Account.builder()
                .holder(user)
                .accountNumber("0000001111100000")
                .balance(BigDecimal.valueOf(1000))
                .currencyCode(CurrencyCode.RUB)
                .isBlocked(false)
                .operations(List.of(operation))
                .build();

        operation.setAccount(account);

        //Act: вызываем метод, который тестируем
        AccountDtoResponse dtoResponse = accountMapper.toDtoResponse(account);
        Assertions.assertNotNull(dtoResponse);
        assertEquals(account.getAccountNumber(), dtoResponse.accountNumber());
        assertEquals(account.getBalance(), dtoResponse.balance());
        assertEquals(account.getCurrencyCode(), dtoResponse.currencyCode());
        assertEquals(account.getHolder().getName(), dtoResponse.holderName());
        assertEquals(account.getHolder().getSurname(), dtoResponse.holderSurname());
    }
}