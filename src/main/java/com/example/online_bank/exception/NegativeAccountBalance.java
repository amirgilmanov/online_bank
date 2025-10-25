package com.example.online_bank.exception;

import java.math.BigDecimal;

public class NegativeAccountBalance extends RuntimeException {
    public NegativeAccountBalance(BigDecimal amount, BigDecimal balance) {
        super("Недостаточно средств на балансе. Сумма к списанию - %s, баланс счета - %s".formatted(amount, balance));
    }
}
