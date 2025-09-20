package com.example.online_bank.service;

import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.exception.NegativeAccountBalance;
import com.example.online_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Класс валидации AccountService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateAccountService {
    private final AccountRepository accountRepository;

//    /**
//     * Проверка существование счета по токену пользователя
//     *
//     * @param token Токен пользователя
//     */
//    public void validateAccountExistsByUserToken(String token) {
//        if (!accountRepository.existsByHolder_Token(token)) {
//            log.warn("Пользователь с токеном {} не найден", token);
//            throw new AccountAccessException("Этот счет не принадлежит пользователю с токеном %s".formatted(token));
//        }
//    }

    /**
     * Проверка на то, чтобы сумма к списанию не была больше чем баланс счета
     *
     * @param accountNumber      Номер счета
     * @param countWithdrawMoney Количество денег к списанию
     * @param account            Номер счета
     */
    public void negativeBalanceCheck(String accountNumber, BigDecimal countWithdrawMoney, Account account) {
        if (account.getBalance().compareTo(countWithdrawMoney) < 0) {
            log.error("Попытка списания со счета - {} на сумму - {}, но недостаточно средств {} ",
                    accountNumber, countWithdrawMoney, account.getBalance());

            throw new NegativeAccountBalance("Недостаточно средств на балансе. Сумма к списанию - %s, баланс счета - %s"
                    .formatted(countWithdrawMoney, account.getBalance()));
        }
    }
}
