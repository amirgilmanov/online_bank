package com.example.online_bank;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.service.UserService;
import com.example.online_bank.testsupport.fixture.UserFixtureTest;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
public class AuthTest {

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    UserFixtureTest userFixtureTest;


    @Autowired
    private UserService userService;


    @BeforeEach
    @Transactional
    void cleanUp() {
        log.info("cleanUp");
        userTestHelper.cleanUpByPhoneNumber("12345678910");
    }

    @Test
    @DisplayName("Успешная аутентификация")
    void successSignIn() {
        log.info("Начало тестовой аутентификации");
        userTestHelper.signUpHelper(signUpDto);

        String pinCode = userService.findPinCodeByPhoneNumber("12345678910");

        log.info("Проводим аутентификацию");
        RestAssured.given()
                .queryParam("phoneNumber", "12345678910")
                .queryParam("pinCode", pinCode)
                .log().all()
                .post("api/sign-in")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Неверно переданный номер телефона")
    void failureSignIn() {

        String incorrectPhoneNumber = "091234567891";

        userTestHelper.signUpHelper(signUpDto);

        String pinCode = userService.findPinCodeByPhoneNumber("12345678910");

        log.info("Проводим неудачную аутентификацию");
        RestAssured.given()
                .queryParam("phoneNumber", incorrectPhoneNumber)
                .queryParam("pinCode", pinCode)
                .log().all()
                .post("api/sign-in")
                .then()
                .log().all()
                .statusCode(401);
    }
}
