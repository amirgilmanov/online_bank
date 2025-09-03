package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;

import java.math.BigDecimal;

/**
 * Создать курс. Например, доллар/рубль 90, то есть за один доллар можно получить 90 рублей
 *
 * @param baseCurrency   Базовая валюта
 * @param targetCurrency Котируемая валюта
 * @param rate           Количество котируемой валюты по отношению к базовой
 */
public record CreateExchangeRateDto(
        CurrencyCode baseCurrency,
        CurrencyCode targetCurrency,
        BigDecimal rate
) {
}
