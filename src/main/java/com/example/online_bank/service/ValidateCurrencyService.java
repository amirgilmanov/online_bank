package com.example.online_bank.service;

import com.example.online_bank.domain.dto.ConvertCurrencyResponse;
import com.example.online_bank.enums.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public BigDecimal processTransaction(
            CurrencyCode accountCurrencyCode,
            CurrencyCode selectedCurrencyCode,
            BiConsumer<String, BigDecimal> operationMethodReference,
            String accountNumberTo,
            BigDecimal amount
    ) {
        BigDecimal finalAmount;
        if (accountCurrencyCode.equals(selectedCurrencyCode)) {
            finalAmount = amount;
            operationMethodReference.accept(accountNumberTo, finalAmount); //accountService.someOneOperationType(*accountNumber, amount*)
        } else {
            ConvertCurrencyResponse convertCurrencyResponse = currencyService.convertCurrency(
                    accountCurrencyCode,
                    selectedCurrencyCode,
                    amount
            );
            finalAmount = convertCurrencyResponse.convertedAmount();
            operationMethodReference.accept(accountNumberTo, finalAmount);
        }
        return finalAmount;
    }
}