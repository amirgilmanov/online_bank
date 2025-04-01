package com.example.online_bank.service;


import com.example.online_bank.dto.OperationInfoDtoResponse;
import com.example.online_bank.entity.Account;
import com.example.online_bank.entity.Operation;
import com.example.online_bank.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import com.example.online_bank.mapper.OperationMapper;
import com.example.online_bank.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountService accountService;
    private final OperationMapper operationMapper;

    /**
     * Создать операцию
     *
     * @param createdAt     время операции
     * @param operationType тип операции
     * @param amount        количество денег
     * @param description   описание
     * @param account       счет(сущность)
     * @param currencyCode  код валюты
     */
    @Transactional()
    public Operation createOperation(
            LocalDateTime createdAt,
            OperationType operationType,
            BigDecimal amount,
            String description,
            Account account,
            CurrencyCode currencyCode) {
        log.info("Создание операции");

        nonNullCheck(createdAt, operationType, amount, account, currencyCode);

        Operation operation = Operation.builder()
                .operationType(operationType)
                .amount(amount)
                .description(description)
                .currencyCode(currencyCode)
                .createdAt(createdAt)
                .account(account)
                .build();

        operationRepository.save(operation);
        return operation;
    }

    /**
     * Найти все операции по счету
     *
     * @param accountNumber Номер счета
     * @return Список операций
     */
    @Transactional(readOnly = true)
    @Deprecated
    public List<Operation> showByAccountNumber(String accountNumber) {
        log.info("Показ всех операций по счету {}", accountNumber);
        return operationRepository.findByAccountNumber(accountNumber).stream()
                .filter(Objects::nonNull)
                .sorted(sortOperationsByDateDesc())
                .toList();
    }

    /**
     * Найти список операций по номеру счета(порционно)
     *
     * @param accountNumber Номер счёта
     * @param page          Номер страница
     * @param size          Размер страницы
     * @return Список операций, отсортированных по дате(desc)
     */
    @Transactional(readOnly = true)
    public List<OperationInfoDtoResponse> findByAccountNumberPortion(String accountNumber, long page, long size) {
        log.info("Показ операций по счету порционно(начало с индекса - {}, размер - {}) {}", accountNumber, page, size);

        return operationRepository.findAllByAccount_AccountNumber(accountNumber, PageRequest.of((int) page, (int) size))
                .stream()
                .filter(Objects::nonNull)
                .sorted(sortOperationsByDateDesc())
                .map(operationMapper::toOperationInfoDto)
                .toList();
    }

    /**
     * @param user Пользователь
     * @return Список операций со всех счетов пользователя, отсортированные по дате(сначала новые, потом старые)
     */
    @Transactional(readOnly = true)
    @Deprecated
    public List<Operation> showAllUserOperation(User user) {
        log.info("Показ операций по пользователю {}", user);

        return accountService.findAllAccountsByHolder(user).stream()
                .filter(checkEmptyOperations())
                .flatMap(account -> account.getOperations().stream())
                .sorted(sortOperationsByDateDesc())
                .toList();
    }

    /**
     * @param user Пользователь
     * @param page индекс отображения
     * @param size размер
     * @return список отфильтрованных операций(порционно)
     */
    @Transactional(readOnly = true)
    public List<Operation> showAllUserOperationPortion(User user, long page, long size) {
        log.info("Показ операций(порционно) по пользователю {}. Начало с индекса {}, размер {}", user, page, size);

        return accountService.findAllAccountsByHolder(user).stream()
                .filter(checkEmptyOperations())
                .flatMap(account -> account.getOperations().stream())
                .sorted(sortOperationsByDateDesc())
                .skip(size * (page - 1))
                .limit(size)
                .toList();
    }

    /**
     * Фильтрация пустых операций
     *
     * @return булево о том что пустое значение или нет
     */
    private Predicate<Account> checkEmptyOperations() {
        return account -> {
            if (account.getOperations().isEmpty()) {
                log.warn("Нет операций у счета {}", account.getAccountNumber());
                return false;
            }
            return true;
        };
    }

    /**
     * @return Сортирует операции по дате создания в убывающем порядке(от новых к старым)
     */
    private Comparator<Operation> sortOperationsByDateDesc() {
        return Comparator.comparing(Operation::getCreatedAt).reversed();
    }

    /**
     * Проверка на non-null значения
     */
    private void nonNullCheck(
            LocalDateTime createdAt,
            OperationType operationType,
            BigDecimal amount,
            Account account,
            CurrencyCode currencyCode
    ) {
        requireNonNull(createdAt, "Дата создания не указана");
        requireNonNull(operationType, "Тип операции не указан");
        requireNonNull(amount, "Количество денег не указано");
        requireNonNull(account, "Не указан номер счета");
        requireNonNull(currencyCode, "Код валюты не указан");
    }
}