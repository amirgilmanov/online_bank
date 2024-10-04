//package com.example.online_bank.service.bank;
//
//import com.example.online_bank.dto.transaction.TransactionDtoRequest;
//import com.example.online_bank.dto.transaction.TransactionDtoResponse;
//import com.example.online_bank.entity.Operation;
//import com.example.online_bank.entity.User;
//import com.example.online_bank.entity.account.AccountV2;
//import com.example.online_bank.enums.CurrencyCode;
//import com.example.online_bank.service.finance.FinanceService;
//import com.example.online_bank.service.finance.FinanceServiceV2;
//import com.example.online_bank.service.operation.OperationService;
//import com.example.online_bank.service.transfer.TransferCurrencyServiceV2;
//import com.example.online_bank.service.account.AccountService;
//import com.example.online_bank.service.account.AccountServiceV2;
//import com.example.online_bank.service.transfer.TransferCurrencyServiceV3;
//import com.example.online_bank.service.user.UserRegistrationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BankService {
//    private final AccountServiceV2 accountServiceV2;
//    private final UserRegistrationService userRegistrationService;
//    private final BankIntegrationService bankIntegrationService;
//    private final FinanceService financeService;
//    private final TransferCurrencyServiceV2 transferCurrencyServiceV2;
//    private final FinanceServiceV2 financeServiceV2;
//
//    public void makePayment(String accountId,
//                            BigDecimal amountMoney,
//                            String description,
//                            String token,
//                            CurrencyCode paymentCode
//    ) {
//        financeService.makePaymentV2(accountId, amountMoney, description, token, paymentCode);
//    }
//
//    //3.2. Показывать всю историю платежей по пользователю: на вход токен.
//    // На выход история операция по всем счетам пользователя в отсортированной по дате.
//    public List<Operation> showAllUserHistory(String token) {
//        return financeService.showAllUserHistory(token);
//    }
//
//    //3.3. Делать зачисление: на вход - номер счета, сумма, описание.
//    // Зачисляет на счет деньги и записывает операцию в историю.
//    public void depositMoney(String accountId, BigDecimal amountMoney, String description, CurrencyCode depositCode) {
//        financeService.depositMoney(accountId, amountMoney, description, depositCode);
//    }
//
//    public TransactionDtoResponse transferToAnotherBank(
//            String recipientAccountId,
//            TransactionDtoRequest dtoRequest,
//            String senderAccountId,
//            String bankName,
//            String userToken
//    ) {
//        return bankIntegrationService.transferMoneyToPartnerBank(
//                dtoRequest,
//                bankName,
//                senderAccountId,
//                recipientAccountId,
//                userToken
//        );
//    }
//
//    public String info() {
//        return bankIntegrationService.info();
//    }
//
//    public BigDecimal buyCurrency(String baseAccountId,
//                            String targetAccountId,
//                            BigDecimal amountMoney,
//                            String token,
//                            CurrencyCode currencyCode) {
//        AccountV2 account1 = accountServiceV2.findById(baseAccountId);
//        AccountV2 account2 = accountServiceV2.findById(targetAccountId);
//        User user = userRegistrationService.findByToken(token);
//
//        accountServiceV2.checkUserAccount(user, baseAccountId);
//        accountServiceV2.checkUserAccount(user, targetAccountId);
//        makePayment(baseAccountId, amountMoney,
//                "списание со счёта при покупке валюты", token, currencyCode);
//       return transferCurrencyServiceV2
//               .convertCurrency(amountMoney, account1.getCurrencyCode(), account2.getCurrencyCode());
//    }
//}
