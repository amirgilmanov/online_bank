package com.example.online_bank.service;

import com.example.online_bank.domain.dto.ConvertCurrencyResponse;
import com.example.online_bank.domain.dto.RateRequestDto;
import com.example.online_bank.domain.dto.RateResponseDto;
import com.example.online_bank.domain.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.InvertedRateNotFound;
import com.example.online_bank.repository.ExchangeCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final ExchangeCurrencyRepository currencyRepository;

    /**
     * Создает запись курса
     *
     * @param baseCurrency   Валюта, от которой происходит обмен(USD)
     * @param targetCurrency Валюта, к которой происходит обмен(RUB)
     * @param rate           Цена котируемой валюты по отношению к базовой.
     *                       //Пример: доллар рубль 90. Т.е за 1 доллар получим 90 рублей.
     */
    @Transactional
    public RateResponseDto create(
            CurrencyCode baseCurrency,
            CurrencyCode targetCurrency,
            BigDecimal rate) {

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
     * <p>
     * 1) сравниваем полученные данные
     * 2) находим ставку по курсам
     * 3) ставку умножаем на переданное количество
     *
     * @param baseCurrency       Валюта от которой делаем конвертацию
     * @param targetCurrency     Валюта к которой делаем конвертацию
     * @param baseCurrencyAmount Сумма к конвертации
     * @return Информация о конвертации
     */
    public ConvertCurrencyResponse convertCurrency(
            CurrencyCode baseCurrency,
            CurrencyCode targetCurrency,
            BigDecimal baseCurrencyAmount
    ) {
        //1) находим ставку
        BigDecimal baseRate = findRate(baseCurrency, targetCurrency);

        //2) получаем сколько получим целевой (target)
        BigDecimal targetAmount = baseCurrencyAmount.multiply(baseRate);

        //3) собираем ответ
        return new ConvertCurrencyResponse(
                targetCurrency,
                targetAmount,
                baseCurrencyAmount,
                baseCurrency
        );
    }

    /**
     * Найти курс
     *
     * @return Курс базовой валюты по отношению к котируемой валюте
     * Если оказывается что есть только перевернутый курс, то считаем его по формуле 1 / на курс.
     * Например:
     * У нас есть курс доллар - рубль = 90. В метод "найти курс" передали - рубль, доллар.
     * У нас нет валютной пары рубль - доллар, но есть доллар - рубль.
     * Соответственно, мы вернем курс 1 / 90 = 0,01111.
     */
    public ConvertCurrencyResponse findRate(RateRequestDto rateRequestDto) {
        BigDecimal rate = findRate(rateRequestDto.baseCurrency(), rateRequestDto.targetCurrency());
        return new ConvertCurrencyResponse(
                rateRequestDto.targetCurrency(),
                rate,
                ONE,
                rateRequestDto.baseCurrency()
        );
    }

    private BigDecimal findRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        return currencyRepository.findRateByBaseAndTargetCurrency(baseCurrency, targetCurrency)
                .orElseGet(() -> calcInvertedRate(baseCurrency, targetCurrency));
    }

    /**
     * Находим ставку перевернутого курса, делаем расчет курса для изначальной пары валют
     */
    public BigDecimal calcInvertedRate(CurrencyCode baseCurrency, CurrencyCode targetCurrency) {
        log.info("Поиск по перевернутому курсу");
        BigDecimal invertedRate = currencyRepository.findRateByBaseAndTargetCurrency(targetCurrency, baseCurrency)
                .orElseThrow(() -> new InvertedRateNotFound("Перевернутый курс не найден"));
        return ONE.divide(invertedRate, 5, HALF_EVEN);
    }
}