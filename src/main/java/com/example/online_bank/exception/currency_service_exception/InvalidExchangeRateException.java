package com.example.online_bank.exception.currency_service_exception;

import lombok.experimental.StandardException;

/**
 * если переданный курс равен или меньше нулю
 */
@StandardException
public class InvalidExchangeRateException extends RuntimeException {
}
