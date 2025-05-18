package com.example.online_bank;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
public class BankIntegrationTest {

    @Test
    void successGetInfo(){
        RestAssured.given()
                .contentType("application/json")
                .log().all()
                .get("api/integration/get-bank-info")
                .then()
                .log().all()
                .statusCode(200);
    }
    
}
