package com.example.online_bank.entity;

import com.example.online_bank.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  BaseCurrency - Валюта, от которой происходит обмен
 *  TargetCurrency - Валюта, к которой происходит обмен
 *  ExchangeRate - Курс обмена
 */
@Builder
@Data
@AllArgsConstructor
public class ExchangeRate {
    private CurrencyCode baseCurrency;     // Валюта, от которой происходит обмен
    private CurrencyCode targetCurrency;   // Валюта, к которой происходит обмен
    private BigDecimal exchangeRate; // Курс обмена
}
