package com.example.online_bank.controller;

import com.example.online_bank.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class AdviceController {

    /**
     * @param e Обработка ошибки если счет не принадлежит пользователю
     * @return 403 HTTP статус
     */
    @ExceptionHandler(AccountAccessException.class)
    public ResponseEntity<String> handleAccountAccessException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    /**
     * @param e Обработка ошибки при ошибке аутентификации
     * @return 401 HTTP статус
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleUserAuthenticationExceptionException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
    }

    /**
     * @param e Обработка ошибки при ненахождении курса
     * @return 404 HTTP статус
     */
    @ExceptionHandler(CurrencyPairsNotFoundException.class)
    public ResponseEntity<String> handleCurrencyPairsNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    /**
     * @param e Обработка ошибки если сущность уже существует
     * @return 409 статус
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExistsException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    /**
     * @param e обработка ошибки при нулевом балансе
     * @return 400 HTTP статус
     */
    @ExceptionHandler(NegativeAccountBalance.class)
    public ResponseEntity<String> handleNegativeAccountBalanceException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    /**
     * @param e Обработка ошибки в случае возниковения ошибки отправке запроса
     * @return 500  HTTP статус
     */
    @ExceptionHandler(TransferException.class)
    public ResponseEntity<String> handleTransferException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

//    /**
//     * @param e обработка ошибки когда произошла неизвестная ошибка
//     * @return 503 HTTP статус
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleApiException(Exception e) {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("Сервис временно не работает, но мы работаем над этим");
//    }

}
