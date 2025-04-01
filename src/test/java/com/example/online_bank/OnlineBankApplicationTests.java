package com.example.online_bank;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.service.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Commit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Commit
class OnlineBankApplicationTests {
    private SignUpDto signUpDto = new SignUpDto(
            "89992001022",
            "testName",
            "testSurname",
            "testPatronymic"
    );

    @Autowired
    private UserService userService;


    @BeforeEach
    void cleanUp() {
        // userService.deleteByPhoneNumber("89992001022");
    }

    @Test
    @DisplayName("Успешная регистрация")
    void successSignUp() {
        signUpHelper();
    }

    @Test
    @DisplayName("Успешное удаление")
    @Disabled
    void successDelete() {
        RestAssured.given()
                .pathParam("phoneNumber", "89992001022")
                .log().all()
                .delete("/{phoneNumber}")
                .then()
                .log().all()
                .statusCode(OK.value());
    }

    @Test
    @DisplayName("Неудачная регистрация. Сценарий: Пользователь с таким номером уже занят")
    void failSignUp() {
        signUpHelper();
        RestAssured.given()
                .body(signUpDto)
                .contentType("application/json")
                .log().all()
                .post("/api/sign-up")
                .then()
                .log().all()
                .statusCode(CONFLICT.value());
    }

    private void signUpHelper() {
        RestAssured.given()
                .body(signUpDto)
                .contentType("application/json")
                .log().all()
                .post("/api/sign-up")
                .then()
                .log().all()
                .statusCode(CREATED.value());
    }


}
