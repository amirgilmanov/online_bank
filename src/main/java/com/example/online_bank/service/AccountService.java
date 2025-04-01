package com.example.online_bank.service;

import com.example.online_bank.entity.Account;
import com.example.online_bank.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.online_bank.util.CodeGeneratorUtil.generateAccountNumber;


/**
 * Сервис по работе со счетами
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final ValidateAccountService validateAccountService;

    /**
     * Создать счет для пользователя
     *
     * @param user         Пользователь
     * @param currencyCode Код валюты
     */
    @Transactional()
    public Account createAccountForUser(User user, CurrencyCode currencyCode) {
        Account account = Account
                .builder()
                .balance(BigDecimal.ZERO)
                .holder(user)
                .accountNumber(generateAccountNumber(currencyCode))
                .build();
        accountRepository.save(account);
        return account;
    }

    /**
     * Занести деньги на счет. Увеличивает остаток счета. Если счета не существует - ошибка.
     *
     * @param accountNumber Номер счета
     * @param deposit       Сумма пополнения
     */
    //2.3. Занести деньги насчет (номер счета, сумма).
    //Увеличивает остаток счета. Если счета не существует - ошибка.
    @Transactional()
    public void depositMoney(String accountNumber, BigDecimal deposit) {
        log.info("Пополнение счета {} на сумму {}", accountNumber, deposit);
        Account account = findByAccountNumber(accountNumber);

        account.setBalance(account.getBalance().add(deposit));
        accountRepository.save(account);
        log.info("Обновленный баланс после пополнения - {}", account.getBalance());
    }

    /**
     * Уменьшает остаток счета. Остаток после операции не может быть отрицательный.
     * Если счета не существует - ошибка.
     *
     * @param accountNumber      Номер счета
     * @param countWithdrawMoney количество денег для списания
     */
    @Transactional
    public void withdrawMoney(String accountNumber, BigDecimal countWithdrawMoney) {
        log.info("Списание со счета {} на сумму {}", accountNumber, countWithdrawMoney);
        Account account = findByAccountNumber(accountNumber);

        validateAccountService.negativeBalanceCheck(accountNumber, countWithdrawMoney, account);
        account.setBalance(account.getBalance().subtract(countWithdrawMoney));
        accountRepository.save(account);
        log.info("Обновленный баланс после списания - {}", account.getBalance());
    }

    /**
     * Найти все счета пользователя
     *
     * @param holder Пользователь
     * @return Список всех счетов пользователя
     */
    @Transactional(readOnly = true)
    public List<Account> findAllAccountsByHolder(User holder) {
        return accountRepository.findAllByHolder(holder).stream()
                .toList();
    }

    /**
     * Найти баланс по счету
     *
     * @param accountNumber Номер счета
     * @return Сумма баланса на счете
     */
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {
        return findByAccountNumber(accountNumber).getBalance();
    }


    /**
     * Найти по номеру счета
     *
     * @param accountNumber Номер счета
     * @return Сущность Account или выбрасывает ошибку если не удалось найти сущность.
     */
    @Transactional(readOnly = true)
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Счета с таким номером не найдено"));
    }
}
