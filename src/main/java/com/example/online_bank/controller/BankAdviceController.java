package com.example.online_bank.controller;

import com.example.online_bank.exception.account_exception.NegativeAccountBalance;
import com.example.online_bank.exception.currency_service_exception.old_version_exception
        .FreeCurrencyIntegrationException;
import com.example.online_bank.exception.operation_exception.OperationsNotFoundException;
import com.example.online_bank.exception.user_exception.UserAuthenticationException;
import com.example.online_bank.exception.user_exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice
public class BankAdviceController {

    /**
     * @param e обработка ошибки когда счёт с номером не был найден
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Счёта с таким номером не найдено");
    }

    /**
     * @param e обработка ошибки при нулевом балансе
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(NegativeAccountBalance.class)
    public ResponseEntity<String> handleNegativeAccountBalanceException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage() + "Недостаточно средств на счёте");
    }

    /**
     * @param e обработка ошибки при ошибке аутентификации
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<String> handleUserAuthenticationExceptionException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Ошибка аутентификации. Неверный пин-код.");
    }

    /**
     * @param e обработка ошибки когда пользователь был не найден
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserExceptionException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Пользователь с таким Id не найден");
    }

    /**
     * @param e обработка ошибки когда не удалось найти операции по номеру переданному номеру счёта
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(OperationsNotFoundException.class)
    public ResponseEntity<String> handleOperationNotFoundExceptionException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Операция с данным номером счёта не найдена");
    }

    /**
     * @param e обработка ошибки когда произошла неизвестная ошибка
     * @return возвращает 503 HTTP статус
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleApiException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Сервис временно не работает, но мы работаем над этим");
    }

    @ExceptionHandler(FreeCurrencyIntegrationException.class)
    public ResponseEntity<String> handleFreeCurrencyIntegrationException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
