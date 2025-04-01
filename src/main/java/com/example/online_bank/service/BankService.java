package com.example.online_bank.service;

import com.example.online_bank.dto.*;
import com.example.online_bank.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.mapper.AccountMapper;
import com.example.online_bank.mapper.OperationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final AccountService accountService;
    private final BankIntegrationService bankIntegrationService;
    private final FinanceService financeService;
    private final OperationService operationService;
    private final UserService userService;
    private final OperationMapper operationMapper;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountDtoResponse createAccount(String token, CurrencyCode currencyCode) {
        User user = userService.findByToken(token);
        return accountMapper.toDtoResponse(accountService.createAccountForUser(user, currencyCode));
    }

    @Transactional
    public OperationDtoResponse withdraw(String token, FinanceOperationDto financeOperationDto) {
        return financeService.withdrawMoney(token, financeOperationDto, false);
    }

    //3.3. Делать зачисление: на вход - номер счета, сумма, описание.
    // Зачисляет насчет деньги и записывает операцию в историю.
    @Transactional
    public OperationDtoResponse deposit(String userToken, FinanceOperationDto financeOperationDto) {
        return financeService.depositMoney(userToken, financeOperationDto, true);
    }

    @Transactional
    public List<OperationDtoResponse> transfer(TransactionDto transactionDto) {
        return bankIntegrationService.transferMoney(transactionDto);
    }

    public String info() {
        return bankIntegrationService.getBankInfo();
    }

    /**
     * Купить валюту с одного счета пользователя на другой.
     * <p>
     * Производит списание суммы со счета1, делает конвертацию в валюту счета2.
     * <p>
     * Проверяет, что счета принадлежат пользователю (получить счет на основании токена).
     *
     * @param dto   Номер счета с которого произойдет списание, номер счета куда пополнится сумма, сумма
     * @param token токен пользователя
     */
    @Transactional
    public List<OperationDtoResponse> buyCurrency(BuyCurrencyDto dto, String token) {
        return bankIntegrationService.buyCurrency(dto, token);
    }
}
