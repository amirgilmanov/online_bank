package com.example.online_bank;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Commit
@Slf4j
public class AccountTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/currency.csv")
    @DisplayName("Параметризованное создание счета в разных валютах")
    void successAccountCreateParametrized(String currencyName) {
        log.info("Код валюты {}", currencyName);
        RestAssured.given()
                .header(new Header("token", "online358cc527-5805-43de-811d-a9f6e2aa5cbctoken"))
                .queryParam("currencyCode", currencyName)
                .log().all()
                .post("api/account")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("Неудачное создание счета(передан не существующий код валюты)")
    void failureAccountCreation() {
        RestAssured.given()
                .header(new Header("token", "online358cc527-5805-43de-811d-a9f6e2aa5cbctoken"))
                .queryParam("currencyCode", "USB")
                .log().all()
                .post("api/account")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("Получение баланса по существующему номеру счета")
    void successGetBalance() {
        RestAssured.given()
                .pathParam("accountNumber", "840039083")
                .log().all()
                .get("/api/account/{accountNumber}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение баланса по номеру счета, который не существует")
    void failedGetBalance() {
        RestAssured.given()
                .pathParam("accountNumber", "840840840840")
                .log().all()
                .get("/api/account/{accountNumber}")
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }
}
