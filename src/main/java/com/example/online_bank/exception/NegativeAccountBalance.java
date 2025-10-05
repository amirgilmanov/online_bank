package com.example.online_bank.exception;

import lombok.experimental.StandardException;

import java.math.BigDecimal;

@StandardException
public class NegativeAccountBalance extends RuntimeException {
    public NegativeAccountBalance(BigDecimal amount, BigDecimal balance) {
        super("Недостаточно средств на балансе. Сумма к списанию - %s, баланс счета - %".formatted(amount, balance));
    }
}
