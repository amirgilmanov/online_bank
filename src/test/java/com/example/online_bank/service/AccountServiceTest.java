package com.example.online_bank.service;

import com.example.online_bank.domain.dto.AccountDtoResponse;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.mapper.AccountMapper;
import com.example.online_bank.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountForUser() {
        //Arrange: Подготовка данных
        UUID uuidMock = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User userMock = User.builder()
                .id(1L)
                .uuid(uuidMock)
                .build();
        when(userService.findByUuid(uuidMock)).thenReturn(Optional.of(userMock));

        Account accountMock = Account.builder()
                .id(1L)
                .balance(BigDecimal.ZERO)
                .holder(userMock)
                .build();
        when(accountRepository.save(any(Account.class))).thenReturn(accountMock);

        //Act: вызываем метод, который тестируем
        AccountDtoResponse dtoMock = new AccountDtoResponse(
                accountMock.getAccountNumber(),
                accountMock.getCurrencyCode(),
                accountMock.getBalance(),
                accountMock.getHolder().getName(),
                accountMock.getHolder().getSurname()
        );
        when(accountMapper.toDtoResponse(any(Account.class))).thenReturn(dtoMock);
        AccountDtoResponse result = accountService.createAccountForUser(uuidMock, CurrencyCode.CNY);

        //Assert: проверяем результат
        Assertions.assertEquals(AccountDtoResponse.class, result.getClass());
        Assertions.assertNotNull(result);
    }

    @Test
    void depositMoney() {
    }

    @Test
    void withdrawMoney() {
    }

    @Test
    void findAllByHolder() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void findByAccountNumber() {
    }

    @Test
    void findCurrencyCode() {
    }
}