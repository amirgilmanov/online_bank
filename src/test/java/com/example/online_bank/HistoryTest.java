package com.example.online_bank;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@AutoConfigureMockMvc
@Commit
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HistoryTest {
    private final String accountWithOperations = "810097622";
    private final String accountWithoutOperations = "840052474";
    private final String notExistsAccount = "1212121";
    private final String validToken = "online358cc527-5805-43de-811d-a9f6e2aa5cbctoken";
    private final String invalidToken = "qqq1";
    private final String headerName = "token";
    private final String noContentAccountsUserToken = "online-test-token";

    @Test
    @DisplayName("Успешное нахождение всех операций по номеру счета")
    void getSuccessAllOperation() {
        RestAssured.given()
                .queryParam("accountNumber", accountWithOperations)
                .queryParam("page", "1")
                .queryParam("size", "8")
                .log().all()
                .get("/api/history/find-all-by-account-number")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Нет операций по указанному номеру")
    void emptyOperations() {
        RestAssured.given()
                .queryParam("accountNumber", accountWithoutOperations)
                .queryParam("page", "1")
                .queryParam("size", "8")
                .log().all()
                .get("/api/history/find-all-by-account-number")
                .then()
                .log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    @DisplayName("Неудачный просмотр всех операций(номер счета не существует)")
    void failGetSuccessAllOperation() {
        RestAssured.given()
                .queryParam("accountNumber", notExistsAccount)
                .queryParam("page", "1")
                .queryParam("size", "8")
                .log().all()
                .get("/api/history/find-all-by-account-number")
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @DisplayName("Успешный просмотр операций пользователя по всем счетам")
    void successGetAllOperationByHolder() {
        RestAssured.given()
                .header(new Header(headerName, validToken))
                .queryParam("page", "1")
                .queryParam("size", "8")
                .log().all()
                .get("/api/history/find-all-operation-by-user")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Неудачный просмотр операций(Неверный токен)")
    void failGetSuccessAllOperationByHolder() {
        RestAssured.given()
                .header(new Header(headerName, invalidToken))
                .queryParam("page", "1")
                .queryParam("size", "8")
                .log().all()
                .get("/api/history/find-all-operation-by-user")
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @DisplayName("Успешный просмотр счетов пользователя")
    void successGetSuccessAllAccountsByUser() {
        RestAssured.given()
                .header(new Header(headerName, validToken))
                .log().all()
                .get("/api/history/find-all-account-by-user")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Неудачный поиск счетов(неверный токен)")
    void failGetSuccessAllAccountsByUser() {
        RestAssured.given()
                .header(new Header(headerName, invalidToken))
                .log().all()
                .get("/api/history/find-all-account-by-user")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Неудачный поиск счетов(нет созданных счетов)")
    void noContentAccountsByUser() {
        RestAssured.given()
                .header(new Header(headerName, noContentAccountsUserToken))
                .log().all()
                .get("/api/history/find-all-account-by-user")
                .then()
                .log().all()
                .statusCode(204);
    }

}
