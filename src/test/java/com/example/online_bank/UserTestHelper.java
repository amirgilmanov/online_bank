package com.example.online_bank;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.CREATED;

@Service
@Getter
@Slf4j
public class UserTestHelper {

    @Autowired
    private UserService userService;

    @Transactional
    public void cleanUpByPhoneNumber(String phoneNumber) {
        log.info("Cleaning up");
        if (userService.existsByPhoneNumber(phoneNumber)) {
            userService.deleteByPhoneNumber(phoneNumber);
        }
    }

    public void signUpHelper(SignUpDto signUpDto) {
        ValidatableResponse validatableResponse = RestAssured.given()
                .body(signUpDto)
                .contentType("application/json")
                .log().all()
                .post("/api/sign-up")
                .then()
                .log().all()
                .statusCode(CREATED.value());
    }
}
