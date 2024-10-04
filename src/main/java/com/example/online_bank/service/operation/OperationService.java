package com.example.online_bank.service.operation;


import com.example.online_bank.entity.Operation;
import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import com.example.online_bank.exception.operation_exception.OperationsNotFoundException;
import com.example.online_bank.repository.operation.OperationRepository;

import com.example.online_bank.service.account.AccountServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.example.online_bank.enums.OperationType.DEPOSIT;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountServiceV2 accountServiceV2;

//3 Этап - создание операций
//1. Создать сущность операция - уникальный идентификатор, дата+время (равна времени создания операции), номер счета,
// тип операции (списание/зачисление),
// сумма, описание. Операции должны сохранятся в приложении.
//2. Создать сервис операций. Умеет:
//2.1 Показывать все операции по счету. Порядок операций по дате (сначала новые, потом более старые).
//2.2. Заносить новую операцию.
//2.3 Показывать все операции для пользователя. На вход пользователь.
// Получаем счета пользователя (этап2 пункт 2.5), ищем по этим счетам операции.
// Порядок операций по дате (сначала новые, потом более старые).

    // TODO доделать метод
    public List<Operation> showAllOperations(String accountId) {
        List<Operation> operations = operationRepository.findByAccountId(accountId).stream()
                .sorted(Comparator.comparing(Operation::getCreatedAt).reversed())
                .toList();
        if (operations.isEmpty()) {
            throw new OperationsNotFoundException("нет операций по этому счету");
        }
        return operations;
    }

    // final UUID id;
    //    private LocalDateTime createdAt;
    //    private String accountId;
    //    private OperationType operationType;
    //    private BigDecimal amountMoney;
    //    private String description;
    //    private CurrencyCode currencyCode;
    //2.2. Заносить новую операцию.
    public Operation createOperation(
            UUID id,
            LocalDateTime createdAt,
            String accountId,
            OperationType operationType,
            BigDecimal amountMoney,
            String description,
            CurrencyCode currencyCode) {
        Operation operation = Operation.builder()
                .id(UUID.randomUUID())
                .operationType(DEPOSIT)
                .accountId(accountId)
                .amountMoney(amountMoney)
                .description(description)
                .currencyCode(currencyCode)
                .createdAt(LocalDateTime.now())
                .build();
        operationRepository.save(operation);
        return operation;
    }

    //2.3 Показывать все операции для пользователя. На вход пользователь. Получаем счета пользователя
    // (этап2 пункт 2.5), ищем по этим счетам операции.
//    Порядок операций по дате (сначала новые, потом более старые).
    public List<Operation> showAllUserOperation(User user) {
        List<AccountV2> allAccountV1s = accountServiceV2.getAllAccounts(user);
        return allAccountV1s.stream()
                .flatMap(accountV1 -> operationRepository.findByAccountId(accountV1.getId()).stream())
                .sorted(Comparator.comparing(Operation::getCreatedAt).reversed())
                .toList();
    }
}
