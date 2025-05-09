package com.example.online_bank;

import com.example.online_bank.dto.FinanceOperationDto;
import com.example.online_bank.service.AccountService;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.math.BigDecimal;

import static com.example.online_bank.enums.CurrencyCode.RUB;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
@Commit
@Slf4j
public class OperationTest {

    @Autowired
    private AccountService accountService;
//
//    @BeforeEach
//    void cleanUp() {
//        accountService.withdrawMoney("810097622", BigDecimal.valueOf(100));
//    }

    @Test
    @DisplayName("Успешное пополнение баланса с одинаковым кодом валюты при пополнении и номера счета")
    @Disabled
    void successReceive() {
        RestAssured.given()
                .header(new Header("token", "online358cc527-5805-43de-811d-a9f6e2aa5cbctoken"))
                .body(
                        new FinanceOperationDto(
                                "810043366",
                                BigDecimal.valueOf(100),
                                "test receive",
                                RUB
                        )
                )
                .contentType("application/json")
                .log().all()
                .post("/api/operation/receive")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Disabled
    @DisplayName("Успешное снятие со счета, валюта при снятии как и у счета")
    void successWithdraw() {
        RestAssured.given()
                .header(new Header("token", "online358cc527-5805-43de-811d-a9f6e2aa5cbctoken"))
                .body(
                        new FinanceOperationDto(
                                "810097622",
                                BigDecimal.valueOf(10),
                                "test withdraw", RUB
                        )
                );
    }

}
