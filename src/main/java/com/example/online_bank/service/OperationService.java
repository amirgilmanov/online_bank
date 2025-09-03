package com.example.online_bank.service;


import com.example.online_bank.domain.dto.OperationInfoDto;

import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.Operation;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import com.example.online_bank.exception.EmptyDataException;
import com.example.online_bank.mapper.OperationMapper;
import com.example.online_bank.repository.OperationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountService accountService;
    private final OperationMapper operationMapper;
    private final UserService userService;

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
            @NonNull LocalDateTime createdAt,
            @NonNull OperationType operationType,
            @NonNull BigDecimal amount,
            @NonNull String description,
            @NonNull Account account,
            @NonNull CurrencyCode currencyCode) {
        log.info("Создание операции");

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
     * Найти список операций по номеру счета(порционно)
     *
     * @param accountNumber Номер счёта
     * @return Список операций, отсортированных по дате(desc)
     */
    @Transactional(readOnly = true)
    public List<OperationInfoDto> findAllByAccount(String accountNumber, int page, int size) {
        log.info("Показ операций по счету {} (начало с индекса - {}, размер - {})", accountNumber, page, size);
        Account account = accountService.findByAccountNumber(accountNumber);

        if (account.getOperations().isEmpty()) {
            log.warn("Нет операций у счета {}", account.getAccountNumber());
            throw new EmptyDataException("Нет операций по счету");
        }

        return operationRepository.findAllByAccount_AccountNumber(
                        accountNumber,
                        createPageRequest(page, size))
                .stream()
                .map(operationMapper::toOperationInfoDto)
                .toList();
    }

//    /**
//     * @param token Токен пользователя
//     * @param page  индекс отображения
//     * @param size  размер
//     * @return список отфильтрованных операций(порционно)
//     */
//    @Transactional(readOnly = true)
//    public List<OperationInfoDto> findAllByUserPaged(String token, int page, int size) {
//        User holder = userService.findByToken(token);
//        log.info("Поиск операций по пользователю {}. Начало с индекса {}, размер {}", holder.getId(), page, size);
//
//        return operationRepository.findAllByAccount_Holder(holder, createPageRequest(page, size)).stream()
//                .map(operationMapper::toOperationInfoDto)
//                .toList();
//    }

    private PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page, size, Sort.by(DESC, "createdAt"));
    }
}