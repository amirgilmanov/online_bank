package com.example.online_bank.service;


import com.example.online_bank.domain.dto.AccountDtoResponse;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.EmptyDataException;
import com.example.online_bank.exception.NegativeAccountBalance;
import com.example.online_bank.mapper.AccountMapper;
import com.example.online_bank.repository.AccountRepository;
import com.example.online_bank.security.jwt.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
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
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;

    /**
     * Создать счет для пользователя
     *
     * @param user         Пользователь
     * @param currencyCode Код валюты
     */
    @Transactional()
    public Account createAccountForUser(User user, CurrencyCode currencyCode) throws BadRequestException {
        Arrays.stream(CurrencyCode.values())
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Переданный код валюты не найден"));

        Account account = Account
                .builder()
                .balance(BigDecimal.ZERO)
                .holder(user)
                .accountNumber(generateAccountNumber(currencyCode))
                .currencyCode(currencyCode)
                .build();
        accountRepository.save(account);
        return account;
    }

    /**
     * Занести деньги на счет. Увеличивает остаток счета. Если счета не существует - ошибка.
     *
     * @param accountNumber Номер счета
     * @param amount        Сумма пополнения
     */
    @Transactional()
    public void depositMoney(String accountNumber, BigDecimal amount) {
        Account account = findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
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
        Account account = findByAccountNumber(accountNumber);
        if (account.getBalance().compareTo(countWithdrawMoney) < 0) {
            throw new NegativeAccountBalance(countWithdrawMoney, account.getBalance());
        }
        account.setBalance(account.getBalance().subtract(countWithdrawMoney));
    }

    /**
     * Найти все счета пользователя
     *
     * @param token Токен пользователя
     * @return Список всех счетов пользователя
     */
    @Transactional(readOnly = true)
    public List<AccountDtoResponse> findAllByHolder(String token) {

        if (holder.getAccounts().isEmpty()) {
            log.warn("Нет счетов у пользователя {}", holder.getId());
            throw new EmptyDataException("Нет счетов у данного пользователя");
        }
        return accountRepository.findAllByHolder(holder).stream()
                .map(accountMapper::toDtoResponse)
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

    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}
