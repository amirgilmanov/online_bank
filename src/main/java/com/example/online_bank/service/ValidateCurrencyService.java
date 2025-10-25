package com.example.online_bank.service;

import com.example.online_bank.domain.dto.ConvertCurrencyResponse;
import com.example.online_bank.enums.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class ValidateCurrencyService {
    private final CurrencyService currencyService;

    /**
     * Сверяет код валюты для снятия с кодом валюты кошелька.
     * Если коды разнятся, то производит конвертацию, возвращает итоговую сумму
     */
    public BigDecimal processTransaction(
            CurrencyCode accountCurrencyCode,
            CurrencyCode selectedCurrencyCode,
            BiConsumer<String, BigDecimal> operation,
            String accountNumberTo,
            BigDecimal amount
    ) {
        BigDecimal finalAmount;
        if (accountCurrencyCode.equals(selectedCurrencyCode)) {
            finalAmount = amount;
            operation.accept(accountNumberTo, finalAmount); //accountService.someOneOperationType(*params*)
        } else {
            ConvertCurrencyResponse convertCurrencyResponse = currencyService.convertCurrency(
                    accountCurrencyCode,
                    selectedCurrencyCode,
                    amount
            );
            finalAmount = convertCurrencyResponse.convertedRate();
            operation.accept(accountNumberTo, finalAmount);
        }
        return finalAmount;
    }
}