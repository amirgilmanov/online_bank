package com.example.online_bank.service;

import com.example.online_bank.domain.dto.OperationInfoDto;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.Operation;
import com.example.online_bank.mapper.OperationMapper;
import com.example.online_bank.repository.AccountRepository;
import com.example.online_bank.repository.OperationRepository;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.online_bank.enums.CurrencyCode.RUB;
import static com.example.online_bank.enums.OperationType.WITHDRAW;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@ExtendWith(MockitoExtension.class)
@Slf4j
class OperationServiceTest {
    @InjectMocks
    OperationService operationService;

    @Mock
    EntityManager entityManager;

    @Mock
    OperationRepository operationRepository;

    @Mock
    OperationMapper operationMapper;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void successCreateOperation() {
        LocalDateTime createdAt = LocalDateTime.now();
        BigDecimal amount = BigDecimal.TEN;
        String description = "testDescr";
        String accountNumber = "0011";

        Account account = Account.builder()
                .currencyCode(RUB)
                .accountNumber("0011")
                .balance(BigDecimal.TEN)
                .id(1L)
                .build();
        when(entityManager.getReference(Account.class, accountNumber)).thenReturn(account);
        Operation operation = operationService.createOperation(
                createdAt,
                WITHDRAW,
                amount,
                description,
                accountNumber,
                RUB
        );

        log.info("{}", operation);

        assertDoesNotThrow(() -> operationService.createOperation(
                createdAt,
                WITHDRAW,
                amount,
                description,
                accountNumber,
                RUB
        ));
        assertNotNull(operation);
        assertEquals(accountNumber, operation.getAccount().getAccountNumber());

    }

    @Test
    void failCreateOperation_AccountNumberNotFound() {
        LocalDateTime createdAt = LocalDateTime.now();
        BigDecimal amount = BigDecimal.TEN;
        String description = "testDescr";
        String accountNumber = "0011";

        when(entityManager.getReference(Account.class, accountNumber)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> operationService.createOperation(
                createdAt,
                WITHDRAW,
                amount,
                description,
                accountNumber,
                RUB
        ));
    }

    @Test
    void successFindByAccountNumber() {
        String accountNumber = "0011";

        Account account = Account.builder()
                .currencyCode(RUB)
                .accountNumber("0011")
                .balance(BigDecimal.TEN)
                .id(1L)
                .build();

        Operation operation1 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(30))
                .createdAt(LocalDateTime.of(2025, 1, 10, 2, 20))
                .currencyCode(RUB)
                .description("testdescr1")
                .id(1L)
                .build();

        Operation operation2 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(30))
                .createdAt(LocalDateTime.of(2025, 1, 11, 2, 20))
                .currencyCode(RUB)
                .description("testdescr2")
                .id(2L)
                .build();

        Operation operation3 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(40))
                .createdAt(LocalDateTime.of(2025, 1, 13, 2, 20))
                .currencyCode(RUB)
                .description("testdescr3")
                .id(3L)
                .build();

        when(accountRepository.existsByAccountNumber(accountNumber)).thenReturn(true);

        when(operationRepository.findAllByAccount_AccountNumber(accountNumber,
                of(0, 3, by(DESC, "createdAt"))))
                .thenReturn(List.of(operation3, operation2, operation1));

        when(operationMapper.toOperationInfoDto(any(Operation.class))).thenAnswer(invocation -> {
            Operation op = invocation.getArgument(0);
            return new OperationInfoDto(op.getId(), op.getCreatedAt(), op.getAccount().getAccountNumber(), op.getOperationType(), op.getAmount(), op.getDescription(), op.getCurrencyCode());
        });

        List<OperationInfoDto> allByAccount = operationService.findAllByAccount(accountNumber, 0, 3);
        log.info("{}", allByAccount);
        Assertions.assertEquals(3, allByAccount.size());
        Assertions.assertEquals(BigDecimal.valueOf(40), allByAccount.getFirst().amount());
    }

    @Test
    void failerFindByAccountNumber_AccountNumberNotFound() {
        when(accountRepository.existsByAccountNumber("0011")).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> operationService.findAllByAccount("0011", 0, 1));
    }

    @Test
    void successFindByUuid() {
        UUID uuid = UUID.randomUUID();

        Account account = Account.builder()
                .currencyCode(RUB)
                .accountNumber("0011")
                .balance(BigDecimal.TEN)
                .id(1L)
                .build();

        Operation operation1 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(30))
                .createdAt(LocalDateTime.of(2025, 1, 10, 2, 20))
                .currencyCode(RUB)
                .description("testdescr1")
                .id(1L)
                .build();

        Operation operation2 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(30))
                .createdAt(LocalDateTime.of(2025, 1, 11, 2, 20))
                .currencyCode(RUB)
                .description("testdescr2")
                .id(2L)
                .build();

        Operation operation3 = Operation.builder()
                .operationType(WITHDRAW)
                .account(account)
                .amount(valueOf(40))
                .createdAt(LocalDateTime.of(2025, 1, 13, 2, 20))
                .currencyCode(RUB)
                .description("testdescr3")
                .id(3L)
                .build();

        when(userRepository.existsByUuid(uuid)).thenReturn(true);

        when(operationRepository.findAllByAccount_Holder_Uuid(
                uuid,
                of(0, 3, by(DESC, "createdAt"))))
                .thenReturn(List.of(operation3, operation2, operation1));

        when(operationMapper.toOperationInfoDto(any(Operation.class))).thenAnswer(invocation -> {
            Operation op = invocation.getArgument(0);
            return new OperationInfoDto(
                    op.getId(),
                    op.getCreatedAt(),
                    op.getAccount().getAccountNumber(),
                    op.getOperationType(),
                    op.getAmount(),
                    op.getDescription(),
                    op.getCurrencyCode());
        });

        List<OperationInfoDto> resultDto = operationService.findAllByUserPaged(uuid, 0, 3);
        Assertions.assertEquals(3, resultDto.size());

        Assertions.assertEquals(BigDecimal.valueOf(40), resultDto.getFirst().amount());
    }

    @Test
    void failerFindByUser_UuidNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.existsByUuid(uuid)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> operationService.findAllByUserPaged(uuid, 0, 3));
    }
}