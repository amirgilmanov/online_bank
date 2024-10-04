package com.example.online_bank.service.finance;

import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.entity.Operation;
import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.account_exception.AccountNotFoundException;
import com.example.online_bank.service.account.AccountServiceV2;
import com.example.online_bank.service.operation.OperationService;
import com.example.online_bank.service.transfer.TransferCurrencyServiceV3;
import com.example.online_bank.service.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.online_bank.enums.OperationType.DEPOSIT;
import static com.example.online_bank.enums.OperationType.WITHDRAW;

@RequiredArgsConstructor
@Service
public class FinanceServiceV2 {
    private final AccountServiceV2 accountServiceV2;
    private final UserRegistrationService userRegistrationService;
    private final OperationService operationService;
    private final TransferCurrencyServiceV3 transferCurrencyServiceV3;

    //3. Создать Банк сервис. Сервис умеет:
    //3.1. Делать платеж: на вход - номер счета, сумма, описание, токен. На основании токена получаем пользователя.
    //Проверяем что счет принадлежит пользователю(этап 2 пункт2.7): если счет не принадлежит пользователю,
    // выкидываем ошибку.
    //Производит списание со счета (2 этап 2.4 пункт). Записывает операцию в историю.
    //3.2. Показывать всю историю платежей по пользователю: на вход токен.
    // На выход история операция по всем счетам пользователя в отсортированной по дате
    // По токену получить пользователя, дальше пункт 2.3.
    //3.3. Делать зачисление: на вход - номер счета, сумма, описание. Зачисляет на счет деньги
    // и записывает операцию в историю.

    //5. Ко всем операциям банк сервиса добавить валюту.
    //В случае если валюта зачисления/списания будет отличаться от валюты счета, то делать конвертацию валют.
    public List<Operation> showAllUserHistory(String token) {
        return operationService.showAllUserOperation(userRegistrationService.findByToken(token));
    }

    public OperationDtoResponse makePayment(
            String accountId,
            BigDecimal amountMoney,
            String description,
            String token,
            CurrencyCode paymentCurrencyCode
    ) {
      checkingParameters(token, accountId, paymentCurrencyCode, amountMoney);
        accountServiceV2.withdrawMoney(accountId, amountMoney);
        Operation operation = operationService.createOperation(
                UUID.randomUUID(),
                LocalDateTime.now(),
                accountId,
                WITHDRAW,
                amountMoney,
                description,
                paymentCurrencyCode);
        return convertEntityToResponse(operation);
    }

    //3.3. Делать зачисление: на вход - номер счета, сумма, описание.
    // Зачисляет на счет деньги и записывает операцию в историю.
    public OperationDtoResponse depositMoney(
            String accountId,
            BigDecimal amountMoney,
            String description,
            String token,
            CurrencyCode depositCurrencyCode) {
        checkingParameters(token, accountId, depositCurrencyCode, amountMoney);
        accountServiceV2.depositMoney(accountId, amountMoney);
        Operation operation = operationService.createOperation(
                UUID.randomUUID(),
                LocalDateTime.now(),
                accountId,
                DEPOSIT,
                amountMoney,
                description,
                depositCurrencyCode);
        return convertEntityToResponse(operation);
    }

    private OperationDtoResponse convertEntityToResponse(Operation operation) {
        return new OperationDtoResponse(operation.getId(),
                operation.getOperationType(),
                operation.getAccountId(),
                operation.getAmountMoney(),
                operation.getDescription(),
                operation.getCreatedAt(),
                operation.getCurrencyCode()
        );
    }

    private void checkingParameters(String token, String accountId, CurrencyCode currencyCode, BigDecimal amountMoney) {
        User user = userRegistrationService.findByToken(token);
        boolean checkUserAccount = accountServiceV2.checkUserAccount(user, accountId);
        if (!checkUserAccount) {
            throw new AccountNotFoundException("Этот счет не принадлежит пользователю");
        }
        AccountV2 accountV2 = accountServiceV2.findById(accountId);
        if (!currencyCode.equals(accountV2.getCurrencyCode())) {
            transferCurrencyServiceV3
                    .convertCurrency(amountMoney, currencyCode, accountV2.getCurrencyCode());
        }
    }
}

