package com.example.online_bank;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.service.UserService;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CONFLICT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Commit
@Slf4j
class SignInTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTestHelper userTestHelper;
    private final SignUpDto signUpDto = new SignUpDto(
            "89992001022",
            "testName",
            "testSurname",
            "testPatronymic"
    );

    @BeforeEach
    void cleanUp() {
        userService.deleteByPhoneNumber("89992001022");
    }

    @Test
    @DisplayName("Успешная регистрация")
    void successSignUp() {
        userTestHelper.signUpHelper(signUpDto);
    }

    @Test
    @DisplayName("Неудачная регистрация. Сценарий: Пользователь с таким номером уже занят")
    void failSignUp() {
        userTestHelper.signUpHelper(signUpDto);
        RestAssured.given()
                .body(new SignUpDto(
                        "89992001022",
                        "testName2",
                        "testSurname3",
                        "testPatronymic3"))
                .contentType("application/json")
                .log().all()
                .post("/api/sign-up")
                .then()
                .log().all()
                .statusCode(CONFLICT.value());
    }
}

