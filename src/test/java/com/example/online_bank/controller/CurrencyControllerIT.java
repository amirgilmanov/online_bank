package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.ConvertCurrencyDto;
import com.example.online_bank.domain.dto.CreateExchangeRateDto;
import com.example.online_bank.domain.dto.RateRequestDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.repository.ExchangeCurrencyRepository;
import com.example.online_bank.service.CurrencyService;
import com.example.online_bank.service.TokenService;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.online_bank.enums.CurrencyCode.RUB;
import static com.example.online_bank.enums.CurrencyCode.USD;
import static io.restassured.http.ContentType.JSON;
import static java.math.BigDecimal.ZERO;
import static java.util.UUID.randomUUID;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
@Transactional
class CurrencyControllerIT {
    private static final String URL = "http://localhost:8081/api/currency";
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ExchangeCurrencyRepository repository;

    String token;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        repository.deleteAll();
        RestAssured.baseURI = URL;
        UserContainer userContainer = new UserContainer(
                randomUUID().toString(),
                "testAdmin",
                List.of("ROLE_ADMIN")
        );
        token = tokenService.getAccessToken(userContainer);
        Thread.sleep(1100);
    }

    @Test
    void successCreateExchangeRate() {
        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .header("Authorization", "Bearer " + token)
                .body(new CreateExchangeRateDto(USD, RUB, BigDecimal.valueOf(90)))
                .post("/create")
                .then()
                .log().all()
                .statusCode(CREATED.value());
    }

    @Test
    void failureCreateExchangeRate() {
        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .header("Authorization", "Bearer " + token)
                .body(new CreateExchangeRateDto(USD, RUB, ZERO))
                .post("/create")
                .then()
                .log().all()
                .statusCode(BAD_REQUEST.value());
    }


    @Test
    void successConvertCurrency_RateWasFound() {
        //arr
        currencyService.create(USD, RUB, BigDecimal.valueOf(90));

        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .body(new ConvertCurrencyDto(USD, RUB, BigDecimal.valueOf(50)))
                .post("/convert")
                .then()
                .log().all()
                .statusCode(OK.value());
    }

    @Test
    void successConvertCurrencyByInvertedRate() {
        currencyService.create(RUB, USD, BigDecimal.valueOf(0.11));
        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .body(new ConvertCurrencyDto(USD, RUB, BigDecimal.valueOf(50)))
                .post("/convert")
                .then()
                .log().all()
                .statusCode(OK.value());
    }

    @Test
    void failureConvertCurrency_RateWasNotFound() {
        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .body(new ConvertCurrencyDto(USD, RUB, BigDecimal.valueOf(50)))
                .post("/convert")
                .then()
                .log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void findRate() {
        RestAssured.given()
                .contentType(JSON)
                .log().all()
                .body(new RateRequestDto(USD, RUB))
                .post("/find-rate")
                .then()
                .log().all()
                .statusCode(BAD_REQUEST.value());
    }
}