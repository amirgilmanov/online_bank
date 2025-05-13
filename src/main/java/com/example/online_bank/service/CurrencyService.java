package com.example.online_bank.service;

import com.example.online_bank.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.CurrencyPairsNotFoundException;
import com.example.online_bank.exception.InvalidRateException;
import com.example.online_bank.repository.ExchangeCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final ExchangeCurrencyRepository currencyRepository;

    /**
     * @param baseCurrency   Валюта, от которой происходит обмен(USD)
     * @param targetCurrency Валюта, к которой происходит обмен(RUB)
     * @param rate           Цена котируемой валюты по отношению к базовой.
     *                       Создает запись курса
     */
    //Курс не может быть равен нулю или отрицательным.
    //Пример: доллар рубль 90. Т.е за 1 доллар получим 90 рублей.
    public void create(CurrencyCode baseCurrency, CurrencyCode targetCurrency, BigDecimal rate) {
        validateSum(rate);
        ExchangeRate entity = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .build();
        currencyRepository.save(entity);
    }

    /**
     * @param baseCurrency   Базовая валюта
     * @param targetCurrency Котируемая валюта
     * @return Курс базовой валюты по отношению к котируемой валюте
     * <p>
     * Если оказывается что есть только перевернутый курс, то считаем его по формуле 1 / на курс.
     * <p>
     * Например:
     * у нас есть курс доллар - рубль = 90. В метод "найти курс" передали - рубль, доллар.
     * У нас нет валютной пары рубль - доллар, но есть доллар - рубль.
     * Соответственно, мы вернем курс 1 / 90 = 0,01111.
     */
    public BigDecimal findRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        if (!currencyRepository.existsByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)) {
            return calcInvertedRate(baseCurrency, targetCurrency);
        } else {
            return checkCurrencyPair(baseCurrency, targetCurrency).getRate();
        }
    }

    /**
     * Произвести конвертацию. Сумма не может быть нулевой или отрицательной
     *
     * @param baseCurrency   Валюта от которой делаем конвертацию
     * @param targetCurrency Валюта к которой делаем конвертацию
     * @param count          Сумма к конвертации
     * @return Сумма купленной валюты
     */
    public BigDecimal convertCurrency(CurrencyCode baseCurrency, CurrencyCode targetCurrency, BigDecimal count) {
        BigDecimal rate = checkCurrencyPair(baseCurrency, targetCurrency).getRate();
        return calcConvertCurrency(count, rate);
    }

    private BigDecimal calcInvertedRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        ExchangeRate invertedExchangeRate = findInvertedRate(baseCurrency, targetCurrency);
        return calcInvertedRateHelper(invertedExchangeRate);
    }

    private ExchangeRate findInvertedRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        return currencyRepository.findCurrencyRate(targetCurrency, baseCurrency)
                .orElseThrow(() -> new CurrencyPairsNotFoundException("Перевернутый курс не найден"));
    }

    private BigDecimal calcInvertedRateHelper(ExchangeRate rate) {
        return BigDecimal.ONE.divide(rate.getRate(), 4, RoundingMode.HALF_EVEN);
    }

    private void validateSum(BigDecimal rate) {
        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidRateException("Курс не может быть равен нулю или отрицательным");
        }
    }

    private ExchangeRate checkCurrencyPair(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        return currencyRepository.findCurrencyRate(baseCurrency, targetCurrency)
                .orElseThrow(() -> new CurrencyPairsNotFoundException("Курс не найден"));
    }

    private BigDecimal calcConvertCurrency(BigDecimal count, BigDecimal rate) {
        BigDecimal convertedCurrency = rate.multiply(count);
        validateSum(convertedCurrency);
        return convertedCurrency;
    }
}
