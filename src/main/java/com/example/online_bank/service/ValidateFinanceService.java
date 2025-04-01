package com.example.online_bank.service;

import com.example.online_bank.enums.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ValidateFinanceService {
    private final AccountService accountService;
    private final ValidateAccountService validateAccountService;
    private final CurrencyService currencyService;

    /**
     * Включает в себя проверки по валютному коду и существование счета по переданному токену
     *
     * @param token              Токен пользователя
     * @param accountNumber      номер счета
     * @param incomeCurrencyCode входящий код валюты
     * @param amount             количество денег
     */
    public void validateParameters(
            String token,
            String accountNumber,
            CurrencyCode incomeCurrencyCode,
            BigDecimal amount
    ) {
        validateAccountService.validateAccountExistsByUserToken(token);
        //допустим счет в рублях а мы хотим снять зимбабвийский доллар в зимбабве через зимбабский банкомат  с нашей
        // с нашей русской карты вводим какую валюты мы хотим снять и сравниваем с валютой счета если разная - то конвертируем
        //пополняем 100000 песо и эти песо конвертируем в 100 рублей
        validateCurrencyCode(incomeCurrencyCode, amount, accountNumber);
    }

    /**
     * Проверяет входящий код валюты с номером кода валюты, который получаем по номеру счета.
     * Если коды разнятся, то производим конвертацию
     *
     * @param incomeCurrencyCode Входящий код валюты
     * @param amount             Количество денег
     * @param accountNumber      Номер счета
     */
    private void validateCurrencyCode(CurrencyCode incomeCurrencyCode, BigDecimal amount, String accountNumber) {
        CurrencyCode accountCurrencyCode = accountService.findByAccountNumber(accountNumber).getCurrencyCode();

        if (!incomeCurrencyCode.equals(accountCurrencyCode)) {
            validateSenderAndRecipientCurrencyCode(incomeCurrencyCode, accountCurrencyCode, amount);
        }
    }

    /**
     * Проверяет код валюты отправителя и получателя.
     * Если код валюты разный, то производится конвертация в валюту получателя
     * <p>
     * Метод используется как при переводе, где есть получатель и отправитель так и при пополнении
     * допустим счет в рублях а мы хотим снять зимбабвийский доллар в зимбабве через зимбабский банкомат  с нашей
     * с нашей русской карты вводим какую валюты мы хотим снять и сравниваем с валютой счета если разная - то конвертируем
     * пополняем 100000 песо и эти песо конвертируем в 100 рублей
     *
     * @param senderCurrencyCode    Код валюты отправителя
     * @param recipientCurrencyCode Код валюты получателя
     * @param amount                Количество денег
     * @return Количество конвертируемых денег если код валюты одинаковый, то возвращается исходное количество
     */
    public BigDecimal validateSenderAndRecipientCurrencyCode(
            CurrencyCode senderCurrencyCode,
            CurrencyCode recipientCurrencyCode,
            BigDecimal amount
    ) {
        if (!senderCurrencyCode.equals(recipientCurrencyCode)) {
            return currencyService.findRate(senderCurrencyCode, recipientCurrencyCode);
        } else {
            return amount;
        }
    }
}
