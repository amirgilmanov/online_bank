package com.example.online_bank.entity.account;

import com.example.online_bank.entity.User;
import com.example.online_bank.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Номер счета (уникален), владелец (класс Пользователь этап1 пункт3), остаток на счете (с копейками).
 */
@AllArgsConstructor
@Builder
@Data
public class AccountV2 {
    private String id;
    private User holder;
    private BigDecimal accountBalance;
    private CurrencyCode currencyCode;
}
