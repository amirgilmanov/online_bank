package com.example.online_bank.service.transfer;

import com.example.online_bank.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.currency_service_exception.CurrencyPairsNotFoundException;
import com.example.online_bank.exception.currency_service_exception.InvalidArgumentsException;
import com.example.online_bank.exception.currency_service_exception.InvalidExchangeRateException;
import com.example.online_bank.repository.currency.ExchangeCurrencyRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferCurrencyServiceV3 {
    private final ExchangeCurrencyRepositoryV3 exchangeCurrencyRepositoryV3;
//  2. Создать сервис по работе с валютой. Имеет следующие методы:
//2.1 Создать обменный курс (на вход валюта1, валюта2, курс за одну единицу (число с пятью знаками после запятой)).
//Создать запись обменного курса для двух валют. Курс не может быть равен нулю или отрицательным.
//Пример: доллар рубль 90. Т.е за 1 доллар получим 90 рублей.

    /**
     * @param baseCurrency   Валюта, из которой происходит обмен
     * @param targetCurrency Валюта, к которой происходит обмен
     * @param exchangeRate   курс обмена, задается самостоятельно
     * @throws InvalidExchangeRateException если переданный курс равен или меньше нулю
     */
    public void createExchangeRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency, BigDecimal exchangeRate) {
        if (baseCurrency == null || targetCurrency == null) {
            throw new InvalidArgumentsException("Аргументы %s %s не могут быть пустыми", baseCurrency, targetCurrency);
        }
        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExchangeRateException("Курс обмена не может быть равным или меньше нулю");
        }
        String currencyPair = createCurrencyPair(baseCurrency, targetCurrency);
        ExchangeRate exchangeCurrencyRate = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .exchangeRate(exchangeRate)
                .build();

        exchangeCurrencyRepositoryV3.save(currencyPair, exchangeCurrencyRate);
        log.debug("Создан курс обмена валют для {} ", currencyPair);
    }

    //2.2. Найти курс (на вход валюта1, валюта2). Ищем по курсам, где эти две валюты совпадают.
//Если оказывается что есть только перевернутый курс, то считаем его по формуле 1 / *
//Например:
//у нас есть курс доллар - рубль = 90. В метод "найти курс" передали - рубль, доллар.
//У нас нет валютной пары рубль - доллар, но есть доллар - рубль. Соответственно, мы вернем курс 1 / 90 = 0,01111.
    private BigDecimal findExchangeRate(CurrencyCode currency1, CurrencyCode currency2) {
        if (currency1 == null || currency2 == null) {
            throw new InvalidArgumentsException("Аргументы %s %s не могут быть пустыми", currency1, currency2);
        }

        String currencyPair = createCurrencyPair(currency1, currency2);
        String reversedCurrencyPair = createCurrencyPair(currency2, currency1);

        if (exchangeCurrencyRepositoryV3.containsPair(currencyPair)) {
            ExchangeRate exchangeRate = exchangeCurrencyRepositoryV3.findByCurrencyPairs(currencyPair).orElseThrow();

            BigDecimal rate = exchangeRate.getExchangeRate();

            log.info("Данный курс({}) был найден и составляет {}", currencyPair, rate);
            return rate;
        } else if (!exchangeCurrencyRepositoryV3.containsPair(currencyPair)
                && exchangeCurrencyRepositoryV3.containsPair(reversedCurrencyPair)) {
            ExchangeRate exchangeRate = exchangeCurrencyRepositoryV3
                    .findByCurrencyPairs(reversedCurrencyPair).orElseThrow();

            BigDecimal rate = exchangeRate.getExchangeRate();
            BigDecimal divedRate = rate.divide(BigDecimal.ONE, RoundingMode.CEILING);

            log.info("Был найден обратный курс {}, который составляет {}", reversedCurrencyPair, divedRate);
            return divedRate;
        }
        log.error("Не удалось найти курсы валют по переданным парам {} {}", currencyPair, reversedCurrencyPair);
        throw new CurrencyPairsNotFoundException("Не удалось найти курсы по этим парам %s, %s",
                currencyPair, reversedCurrencyPair);
    }

    //2.3. Произвести конвертацию
    // (на вход сумма, валюта из которой делаем конвертацию, валюта в которую делаем конвертацию).
//На выход сумма купленной валюты. Используй пункт 1.2. Сумма не может быть нулевой или отрицательной.
    public BigDecimal convertCurrency(BigDecimal amountMoney, CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        if (amountMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExchangeRateException("Переданная сумма меньше или равна нулю");
        }
        BigDecimal rate = findExchangeRate(baseCurrency, targetCurrency);
        return rate.multiply(amountMoney);
    }

    private String createCurrencyPair(CurrencyCode currencyCode1, CurrencyCode currencyCode2) {
        return currencyCode1.name() + "-" + currencyCode2.name();
    }
}
