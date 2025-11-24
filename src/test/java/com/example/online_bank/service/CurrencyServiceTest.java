package com.example.online_bank.service;

import com.example.online_bank.domain.dto.ConvertCurrencyResponse;
import com.example.online_bank.domain.dto.RateResponseDto;
import com.example.online_bank.domain.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.InvalidCountException;
import com.example.online_bank.exception.InvertedRateNotFound;
import com.example.online_bank.repository.ExchangeCurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.online_bank.enums.CurrencyCode.RUB;
import static com.example.online_bank.enums.CurrencyCode.USD;
import static java.math.BigDecimal.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CurrencyServiceTest {
    @Mock
    ExchangeCurrencyRepository exchangeCurrencyRepository;
    @InjectMocks
    CurrencyService currencyService;

    @Test
    void successCreateRate() {
        ExchangeRate savedEntity = ExchangeRate.builder()
                .baseCurrency(USD)
                .targetCurrency(RUB)
                .rate(BigDecimal.valueOf(90))
                .id(1L)
                .build();

         when(exchangeCurrencyRepository.save(any(ExchangeRate.class))).thenReturn(savedEntity);
        RateResponseDto rateResponseDto = currencyService.create(USD, RUB, BigDecimal.valueOf(90));
        log.info("{}", rateResponseDto);
        Assertions.assertEquals(BigDecimal.valueOf(90), rateResponseDto.rate());
    }

    @Test
    void successConvertCurrency() {
        //доллары в рубли
        CurrencyCode baseCurrency = USD;
        CurrencyCode targetCurrency = RUB;
        BigDecimal amount = BigDecimal.valueOf(100);

        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(USD, RUB)).thenReturn(Optional.of(BigDecimal.valueOf(90)));
        ConvertCurrencyResponse convertCurrencyResponse = currencyService.convertCurrency(baseCurrency, targetCurrency, amount);
        log.info("{}", convertCurrencyResponse);
        BigDecimal rate = convertCurrencyResponse.convertedAmount();

        Assertions.assertEquals(BigDecimal.valueOf(9000), rate);
    }

    @Test
    void successConvertCurrency_ByInvertedRate() {
        //хотим 15$ перевести в рубли
        //курс доллар рубль 90 не найден
        // чтобы найти мы 15 * 90 = 1350
        // есть курс RUB USD 0.01111
        // 1 / 0.01111 = 90.09
        // 15 * 90.09

        BigDecimal amount = BigDecimal.valueOf(15);
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(USD, RUB)).thenReturn(Optional.empty());
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(RUB, USD)).thenReturn(Optional.of(new BigDecimal("0.01111")));

        ConvertCurrencyResponse result = currencyService.convertCurrency(USD, RUB, amount);
        log.info("{}", result);
        Assertions.assertEquals(new BigDecimal("15"), result.amount());
        Assertions.assertEquals(new BigDecimal("1350.13500"), result.convertedAmount());
    }

    @Test
    void failConvert_InvertedRateNotFound() {
        BigDecimal amount = BigDecimal.valueOf(15);
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(USD, RUB)).thenReturn(Optional.empty());
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(RUB, USD)).thenReturn(Optional.empty());

        Assertions.assertThrows(InvertedRateNotFound.class, () -> currencyService.convertCurrency(USD, RUB, amount));
    }

    @Test
    void successCalcInvertRate() {
        //для случая когда курс доллар рубль 90 не найден
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(RUB, USD)).thenReturn(Optional.of(new BigDecimal("0.01111")));
        BigDecimal rate = currencyService.calcInvertedRate(USD, RUB);
        Assertions.assertEquals(new BigDecimal("90.00900"), rate);
    }

    @Test
    void successCalcInvertRate_ForRUB_USD() {
        //для случая когда курс рубль доллар 0.01111 не найден
        when(exchangeCurrencyRepository.findRateByBaseAndTargetCurrency(USD, RUB)).thenReturn(Optional.of(new BigDecimal("90")));
        BigDecimal rate = currencyService.calcInvertedRate(RUB, USD);
        Assertions.assertEquals(new BigDecimal("0.01111"), rate);

    }

    @Test
    void failConvertCurrency_RateIsZero() {
        CurrencyCode baseCurrency = USD;
        CurrencyCode targetCurrency = RUB;
        BigDecimal amount = ZERO;

        Assertions.assertThrows(InvalidCountException.class, () -> currencyService.convertCurrency(baseCurrency, targetCurrency, amount));
    }


    @Test
    void findRate() {
    }
}