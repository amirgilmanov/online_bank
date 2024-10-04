package com.example.online_bank.service.bank;

import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.transaction.TransactionDtoRequest;
import com.example.online_bank.dto.transaction.TransactionDtoResponse;
import com.example.online_bank.entity.Operation;
import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.service.account.AccountServiceV2;
import com.example.online_bank.service.finance.FinanceServiceV2;
import com.example.online_bank.service.transfer.TransferCurrencyServiceV3;
import com.example.online_bank.service.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceV2 {
    private final AccountServiceV2 accountServiceV2;
    private final UserRegistrationService userRegistrationService;
    private final BankIntegrationService bankIntegrationService;
    private final TransferCurrencyServiceV3 transferCurrencyServiceV3;
    private final FinanceServiceV2 financeServiceV2;

    public OperationDtoResponse makePayment(
            String accountId,
            BigDecimal amountMoney,
            String description,
            String token,
            CurrencyCode paymentCode
    ) {//проверить контроллер
        return financeServiceV2.makePayment(accountId, amountMoney, description, token, paymentCode);
    }

    //3.2. Показывать всю историю платежей по пользователю: на вход токен.
    // На выход история операция по всем счетам пользователя в отсортированной по дате.
    public List<Operation> showAllUserHistory(String token) {
        return financeServiceV2.showAllUserHistory(token);
    }

    //3.3. Делать зачисление: на вход - номер счета, сумма, описание.
    // Зачисляет на счет деньги и записывает операцию в историю.
    public OperationDtoResponse depositMoney(
            String accountId,
            BigDecimal amountMoney,
            String token,
            String description,
            CurrencyCode depositCode
    ) {
        return financeServiceV2.depositMoney(accountId, amountMoney, description, token, depositCode);
    }

    public TransactionDtoResponse transferToAnotherBank(
            String recipientAccountId,
            TransactionDtoRequest dtoRequest,
            String senderAccountId,
            String senderToken,
            String recipientToken
    ) {
        return bankIntegrationService.transferMoneyToPartnerBank(
                dtoRequest,
                senderAccountId,
                recipientAccountId,
                senderToken,
                recipientToken
        );
    }

    public String info() {
        return bankIntegrationService.info();
    }

    public BigDecimal buyCurrency(String accountId1,
                                  String accountId2,
                                  BigDecimal amountMoney,
                                  String token,
                                  CurrencyCode currencyCode) {
        AccountV2 account1 = accountServiceV2.findById(accountId1);
        AccountV2 account2 = accountServiceV2.findById(accountId2);
        User user = userRegistrationService.findByToken(token);

        accountServiceV2.checkUserAccount(user, accountId1);
        accountServiceV2.checkUserAccount(user, accountId2);
        makePayment(accountId1, amountMoney, "списание со счёта при покупке валюты", token, currencyCode);
        return transferCurrencyServiceV3
                .convertCurrency(amountMoney, account1.getCurrencyCode(), account2.getCurrencyCode());
    }
}
