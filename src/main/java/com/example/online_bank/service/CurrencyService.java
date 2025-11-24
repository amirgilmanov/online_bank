package com.example.online_bank.service;

import com.example.online_bank.domain.dto.ConvertCurrencyResponse;
import com.example.online_bank.domain.dto.RateResponseDto;
import com.example.online_bank.domain.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.InvalidCountException;
import com.example.online_bank.exception.InvertedRateNotFound;
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
     *                       //Пример: доллар рубль 90. Т.е за 1 доллар получим 90 рублей.
     */
    public RateResponseDto create(CurrencyCode baseCurrency, CurrencyCode targetCurrency, BigDecimal rate) {
        validateRateOrCount(rate);
        ExchangeRate entity = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .build();
        currencyRepository.save(entity);
        return new RateResponseDto(baseCurrency, targetCurrency, rate);
    }

    /**
     * Произвести конвертацию. Сумма не может быть нулевой или отрицательной
     *
     * @param baseCurrency   Валюта от которой делаем конвертацию
     * @param targetCurrency Валюта к которой делаем конвертацию
     * @param amount         Сумма к конвертации
     * @return Сумма купленной валюты
     */
    public ConvertCurrencyResponse convertCurrency(CurrencyCode baseCurrency, CurrencyCode targetCurrency, BigDecimal amount) {
        validateRateOrCount(amount);
        ConvertCurrencyResponse convertResult = findRate(baseCurrency, targetCurrency);
        BigDecimal rate = convertResult.convertedAmount();
        return new ConvertCurrencyResponse(targetCurrency, amount.multiply(rate), amount, baseCurrency);
    }

    /**
     * Найти курс
     *
     * @param baseCurrency   Базовая валюта
     * @param targetCurrency Котируемая валюта
     * @return Курс базовой валюты по отношению к котируемой валюте
     * Если оказывается что есть только перевернутый курс, то считаем его по формуле 1 / на курс.
     * Например:
     * у нас есть курс доллар - рубль = 90. В метод "найти курс" передали - рубль, доллар.
     * У нас нет валютной пары рубль - доллар, но есть доллар - рубль.
     * Соответственно, мы вернем курс 1 / 90 = 0,01111.
     */
    public ConvertCurrencyResponse findRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        BigDecimal rate = currencyRepository.findRateByBaseAndTargetCurrency(baseCurrency, targetCurrency)
                .orElseGet(() -> calcInvertedRate(baseCurrency, targetCurrency));
        return new ConvertCurrencyResponse(targetCurrency, rate, BigDecimal.ONE, baseCurrency);
    }

    /**
     * Находим ставку перевернутого курса, делаем расчет курса для изначальной пары валют
     */
    public BigDecimal calcInvertedRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        BigDecimal invertedRate = currencyRepository.findRateByBaseAndTargetCurrency(targetCurrency, baseCurrency)
                .orElseThrow(() -> new InvertedRateNotFound("Перевернутый курс не найден"));
        return BigDecimal.ONE.divide(invertedRate, 5, RoundingMode.HALF_EVEN);
    }

    /**
     * Проверка на, то введенное пользователем количество для конвертации
     */
    private void validateRateOrCount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidCountException("Неверное переданное количество к переводу");
        }
    }
}