package com.example.online_bank;

import com.example.online_bank.dto.CreateExchangeRateDto;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.example.online_bank.enums.CurrencyCode.RUB;
import static com.example.online_bank.enums.CurrencyCode.USD;
import static java.math.BigDecimal.ZERO;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
public class CurrencyTest {
    private final BigDecimal rate = BigDecimal.valueOf(90);
    private final BigDecimal invalidRate = ZERO;

    @Test
    @DisplayName("Успешное создание курса валют")
    void successCreateExchangeRate() {
        RestAssured.given()
                .contentType("application/json")
                .log().all()
                .body(new CreateExchangeRateDto(USD, RUB, rate))
                .post("/api/currency-transfer/create-exchange-rate")
                .then()
                .log().all()
                .statusCode(201);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/currency_rate.csv")
    @DisplayName("Неудачное создание курса(0 и -1)")
    void failCreateExchangeRate(BigDecimal invalidRate) {
        RestAssured.given()
                .contentType("application/json")
                .log().all()
                .body(new CreateExchangeRateDto(USD, RUB, invalidRate))
                .post("/api/currency-transfer/create-exchange-rate")
                .then()
                .log().all()
                .statusCode(400);
    }
}
