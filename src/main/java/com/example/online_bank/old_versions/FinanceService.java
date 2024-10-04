//package com.example.online_bank.service.finance;
//
//import com.example.online_bank.entity.Operation;
//import com.example.online_bank.entity.User;
//import com.example.online_bank.entity.account.AccountV2;
//import com.example.online_bank.enums.CurrencyCode;
//import com.example.online_bank.exception.account_exception.AccountNotFoundException;
//import com.example.online_bank.service.operation.OperationService;
//import com.example.online_bank.service.account.AccountService;
//import com.example.online_bank.service.account.AccountServiceV2;
//import com.example.online_bank.service.transfer.TransferCurrencyServiceV2;
//import com.example.online_bank.service.transfer.TransferCurrencyServiceV3;
//import com.example.online_bank.service.user.UserRegistrationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//import static com.example.online_bank.enums.OperationType.DEPOSIT;
//import static com.example.online_bank.enums.OperationType.WITHDRAW;
//
//@Service
//@RequiredArgsConstructor
//public class FinanceService {
//   // private final AccountService accountService;
//    private final AccountServiceV2 accountServiceV2;
//    private final UserRegistrationService userRegistrationService;
//    private final OperationService operationService;
//    private final TransferCurrencyServiceV2 transferCurrencyServiceV2;
//    //private final TransferCurrencyServiceV3 transferCurrencyServiceV3;
//
//    //3. Создать Банк сервис. Сервис умеет:
//    //3.1. Делать платеж: на вход - номер счета, сумма, описание, токен. На основании токена получаем пользователя.
//    //Проверяем что счет принадлежит пользователю(этап 2 пункт2.7): если счет не принадлежит пользователю,
//    // выкидываем ошибку.
//    //Производит списание со счета (2 этап 2.4 пункт). Записывает операцию в историю.
//    //3.2. Показывать всю историю платежей по пользователю: на вход токен.
//    // На выход история операция по всем счетам пользователя в отсортированной по дате
//    // По токену получить пользователя, дальше пункт 2.3.
//    //3.3. Делать зачисление: на вход - номер счета, сумма, описание. Зачисляет насчет деньг
//    // и записывает операцию в историю.
//
//    //5. Ко всем операциям банк сервиса добавить валюту.
//    //В случае если валюта зачисления/списания будет отличаться от валюты счета, то делать конвертацию валют.
////    public void makePaymentV2(
////            String accountId,
////            BigDecimal amountMoney,
////            String description,
////            String token,
////            CurrencyCode paymentCurrencyCode
////    ) {
////        User user = userRegistrationService.findByToken(token);
////        boolean checkUserAccount = accountService.checkUserAccount(user, accountId);
////        AccountV2 accountV2 = accountServiceV2.findById(accountId);
////        if (!paymentCurrencyCode.equals(accountV2.getCurrencyCode())) {
////            transferCurrencyServiceV2
////                    .convertCurrency(amountMoney, paymentCurrencyCode, accountV2.getCurrencyCode());
////        }
////        if (!checkUserAccount) {
////            throw new AccountNotFoundException("этот счет не принадлежит пользователю");
////        }
////        accountService.withdrawMoney(accountId, amountMoney);
////        operationService.createOperation(Operation.builder()
////                .id(UUID.randomUUID())
////                .operationType(WITHDRAW)
////                .accountId(accountId)
////                .amountMoney(amountMoney)
////                .description(description)
////                .createdAt(LocalDateTime.now())
////                .currencyCode(paymentCurrencyCode)
////                .build());
////    }
//
////    public void makePaymentV2(
////            String accountId,
////            BigDecimal amountMoney,
////            String description,
////            String token
////    ) {
////        User user = userRegistrationService.findByToken(token);
////        boolean checkUserAccount = accountService.checkUserAccount(user, accountId);
////        AccountV2 accountV2 = accountServiceV2.findById(accountId);
////        if (!checkUserAccount) {
////            throw new AccountNotFoundException("этот счет не принадлежит пользователю");
////        }
////        accountService.withdrawMoney(accountId, amountMoney);
////        operationService.createOperation(Operation.builder()
////                .id(UUID.randomUUID())
////                .operationType(WITHDRAW)
////                .accountId(accountId)
////                .amountMoney(amountMoney)
////                .description(description)
////                .createdAt(LocalDateTime.now())
////                .build());
////    }
//
////    public List<Operation> showAllUserHistory(String token) {
////        return operationService.showAllUserOperation(userRegistrationService.findByToken(token));
////    }
//
//    //3.3. Делать зачисление: на вход - номер счета, сумма, описание.
//    // Зачисляет на счет деньги и записывает операцию в историю.
//    public void depositMoney(
//            String accountId,
//            BigDecimal amountMoney,
//            String description,
//            CurrencyCode depositCurrencyCode) {
//        AccountV2 accountV2 = accountServiceV2.findById(accountId);
//        if (!depositCurrencyCode.equals(accountV2.getCurrencyCode())) {
//            transferCurrencyServiceV2.convertCurrency(amountMoney, depositCurrencyCode, accountV2.getCurrencyCode());
//        }
//        accountService.depositMoney(accountId, amountMoney);
//        operationService.createOperation(
//                Operation.builder()
//                        .id(UUID.randomUUID())
//                        .operationType(DEPOSIT)
//                        .accountId(accountId)
//                        .amountMoney(amountMoney)
//                        .description(description)
//                        .currencyCode(depositCurrencyCode)
//                        .createdAt(LocalDateTime.now())
//                        .build());
//    }
//
//    public void depositMoney(
//            String accountId,
//            BigDecimal amountMoney,
//            String description
//    ) {
//        AccountV2 accountV2 = accountServiceV2.findById(accountId);
//        accountService.depositMoney(accountId, amountMoney);
//        operationService.createOperation(
//                Operation.builder()
//                        .id(UUID.randomUUID())
//                        .operationType(DEPOSIT)
//                        .accountId(accountId)
//                        .amountMoney(amountMoney)
//                        .description(description)
//                        .createdAt(LocalDateTime.now())
//                        .build());
//    }
//}
