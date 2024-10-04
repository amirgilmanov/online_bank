package com.example.online_bank.service.account;

import com.example.online_bank.dto.transaction.TransactionDtoRequest;
import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.account_exception.AccountNotFoundException;
import com.example.online_bank.exception.account_exception.NegativeAccountBalance;
import com.example.online_bank.repository.account.AccountRepositoryV2;
import com.example.online_bank.service.other.CodeGeneratorService;
import com.example.online_bank.service.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountServiceV2 {
    private final AccountRepositoryV2 accountRepositoryV2;
    private final CodeGeneratorService codeGeneratorService;

    public String createAccountForUser(User user, CurrencyCode currencyCode) {
        AccountV2 accountV2 = AccountV2
                .builder()
                .accountBalance(BigDecimal.ZERO)
                .holder(user)
                .id(codeGeneratorService.generatedAccountIdV2(currencyCode))
                .currencyCode(currencyCode)
                .build();
        accountRepositoryV2.save(accountV2);
        return accountV2.getId();
    }

    //2.3. Занести деньги насчет (номер счета, сумма).
    //Увеличивает остаток счета. Если счета не существует - ошибка.
    public void depositMoney(String accountId, BigDecimal deposit) {
        AccountV2 accountV2 = accountRepositoryV2.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("счёта с таким номером не найдено"));
        BigDecimal currentBalance = accountV2.getAccountBalance() != null ? accountV2.getAccountBalance()
                : BigDecimal.ZERO;
                accountV2.setAccountBalance(accountV2.getAccountBalance().add(deposit));
    }

    public void withdrawMoney(String accountId, BigDecimal amount) {
        AccountV2 accountV2 = accountRepositoryV2.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
        BigDecimal newBalance = accountV2.getAccountBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAccountBalance("недостаточно средств на балансе");
        } else {
            accountV2.setAccountBalance(newBalance);
        }
    }

    public List<AccountV2> getAllAccounts(User user) {
        return accountRepositoryV2.findByUser(user);
    }

    public BigDecimal getBalance(String id) {
        AccountV2 accountV2 = accountRepositoryV2.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
        return accountV2.getAccountBalance();
    }

    /**
     * Проверка принадлежности счета к пользователю
     *
     * @param user      пользователь
     * @param accountId номер счета
     * @return Возвращает: принадлежит или нет.
     */
    public boolean checkUserAccount(User user, String accountId) {
        AccountV2 accountV2 = accountRepositoryV2.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("счета с таким номером не найдено"));
        return accountV2.getHolder().equals(user);
    }

    public void transferMoney(
            String senderAccountId,
            String recipientAccountId,
            TransactionDtoRequest dto) {
        withdrawMoney(senderAccountId, dto.amount());
        depositMoney(recipientAccountId, dto.amount());
    }

    public AccountV2 findById(String accountId) {
        return accountRepositoryV2.findById(accountId).orElseThrow();
    }
}
