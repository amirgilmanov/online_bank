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
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Commit
@Slf4j
public class UserTests {
    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private UserService userService;

    @BeforeEach
    void cleanUp() {
        log.info("Cleaning up");
        userTestHelper.cleanUpByPhoneNumber("89992001022");
    }

    @Test
    @DisplayName("Успешное удаление")
    void successDelete() {
        userTestHelper.signUpHelper(new SignUpDto("456555", "test", "test", "test"));
        log.info("Создали сущность");
        RestAssured.given()
                .pathParam("phoneNumber", "456555")
                .log().all()
                .delete("/api/user/{phoneNumber}")
                .then()
                .log().all()
                .statusCode(OK.value());
    }
}
