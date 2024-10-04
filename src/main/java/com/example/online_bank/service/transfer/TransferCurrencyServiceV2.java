package com.example.online_bank.service.transfer;

import com.example.online_bank.config.FreeCurrencyIntegrationConfig;
import com.example.online_bank.dto.CurrencyConvertedDtoResponse;
import com.example.online_bank.dto.integration.v1.FreeCurrencyIntegrationDtoResponseV1;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.exception.currency_service_exception.old_version_exception.FreeCurrencyIntegrationException;
import com.example.online_bank.exception.account_exception.NegativeAccountBalance;
import com.example.online_bank.repository.currency.ExchangeCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferCurrencyServiceV2 {
    private final FreeCurrencyIntegrationConfig currencyConfig;
    private final RestTemplate restTemplate;
    private static final String POSTFIX_REQUEST_URL = "currencies=%S&base_currency=%s"; //вторая %s в какую валюту
    private final ExchangeCurrencyRepository exchangeCurrencyRepository;

    public CurrencyConvertedDtoResponse createExchangeCurrencyRate(
            CurrencyCode currencyToConvertName,
            CurrencyCode requestCurrencyName,
            BigDecimal requestCurrencyAmountMoney
    ) {
        if (requestCurrencyAmountMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAccountBalance("Значение суммы валюты должно быть больше нуля");
        }
        try {
            RequestEntity<Void> request = getRequestToFreeCurrency(
                    currencyConfig,
                    requestCurrencyName.name(),
                    currencyToConvertName.name()
            );
            FreeCurrencyIntegrationDtoResponseV1 body = getResponseBody(request);
            BigDecimal convertibleCurrencyRate = body.currencyResponse().get(currencyToConvertName);
            if (convertibleCurrencyRate == null) {
                throw new FreeCurrencyIntegrationException("Курс валюты не найден");
            }
            return createCurrencyConvertedRtoResponse(
                    convertibleCurrencyRate,
                    requestCurrencyName.name(),
                    currencyToConvertName.name(),
                    requestCurrencyAmountMoney
            );
        } catch (FreeCurrencyIntegrationException e) {
            log.error(e.getMessage());
            throw new FreeCurrencyIntegrationException("Ошибка в интеграционном сервисе");
        }
    }

    //2.2. Найти курс (на вход валюта1, валюта2). Ищем по курсам где эти две валюты совпадают.
//Если оказывается что есть только перевернутый курс, то считаем его по формуле 1 / на курс.
//Например:
//у нас есть курс доллар - рубль = 90. В метод "найти курс" передали - рубль, доллар.
//У нас нет валютной пары рубль - доллар, но есть доллар - рубль. Соответственно, мы вернем курс 1 / 90 = 0,01111.
    //если есть пара то такой запрос:

    // https://api.freecurrencyapi.com/v1/latest?apikey=
    // fca_live_8qzrsJhABJpuRPKSIFlajcDS0djycVdmU4BHbiGw&currencies=RUB&base_currency=CNY
    //если нет:  https://api.freecurrencyapi.com/v1/latest?apikey=
    // fca_live_8qzrsJhABJpuRPKSIFlajcDS0djycVdmU4BHbiGw&currencies=CNY&base_currency=RUB

    public BigDecimal findExchangeRate(CurrencyCode currencyFrom, CurrencyCode currencyTo) {

        RequestEntity<Void> voidRequestEntity = exchangeCurrencyRepository
                .check(currencyFrom.name(), currencyTo.name()) ? getRequestToFreeCurrency(
                currencyConfig,
                currencyFrom.name(),
                currencyTo.name()
        )
                : getRequestToFreeCurrency(currencyConfig, currencyTo.name(), currencyFrom.name());
        FreeCurrencyIntegrationDtoResponseV1 body = getResponseBody(voidRequestEntity);

        return exchangeCurrencyRepository.check(
                currencyFrom.name(),
                currencyTo.name()
        ) ? body.currencyResponse().get(currencyFrom.name())
                : body.currencyResponse().get(currencyTo.name());
    }

    private RequestEntity<Void> getRequestToFreeCurrency(
            FreeCurrencyIntegrationConfig currencyConfig,
            String requestCurrencyName,
            String currencyToConvertName
    ) {
        return RequestEntity.get(currencyConfig.getBaseUrl() + POSTFIX_REQUEST_URL
                        .formatted(requestCurrencyName, currencyToConvertName))
                .header(currencyConfig.getHeaderTokenName(), currencyConfig.getToken())
                .build();
    }

    private FreeCurrencyIntegrationDtoResponseV1 getResponseBody(RequestEntity<Void> request) {
        ResponseEntity<FreeCurrencyIntegrationDtoResponseV1> response = restTemplate
                .exchange(request, FreeCurrencyIntegrationDtoResponseV1.class);

        FreeCurrencyIntegrationDtoResponseV1 body = response.getBody();
        if (body == null) {
            throw new FreeCurrencyIntegrationException("Тело не может быть пустым");
        }
        return body;
    }

    private CurrencyConvertedDtoResponse createCurrencyConvertedRtoResponse(
            BigDecimal convertibleCurrencyRate,
            String requestCurrencyName,
            String currencyToConvertName,
            BigDecimal requestCurrencyAmountMoney
    ) {
        BigDecimal convertibleCurrencyAmount = convertibleCurrencyRate.multiply(requestCurrencyAmountMoney);
        exchangeCurrencyRepository.save(requestCurrencyName, currencyToConvertName);
        return new CurrencyConvertedDtoResponse(
                requestCurrencyName,
                currencyToConvertName,
                convertibleCurrencyRate,
                convertibleCurrencyAmount,
                requestCurrencyAmountMoney
        );
    }
    //2.3. Произвести конвертацию (на вход сумма, валюта из которой делаем конвертацию,
    // валюта в которую делаем конвертацию).
    //На выход сумма купленной валюты. Используй пункт 1.2. Сумма не может быть нулевой или отрицательной.

    //https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_8qzrsJhABJpuRPKSIFlajcDS0djycVdmU4BHbiGw&currencies=USD&base_currency=BGN
    public BigDecimal convertCurrency(BigDecimal amount, CurrencyCode currencyFrom, CurrencyCode currencyTo) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAccountBalance("количество переводимых денег не может быть равным нулю");
        }
        BigDecimal rate = findExchangeRate(currencyFrom, currencyTo);
        return rate.multiply(amount);
    }
}



