package com.example.online_bank.enums;

import lombok.Getter;

/**
 * Коды валют
 * рубль - 810, юань - 378, доллар 840
 */
@Getter
public enum CurrencyCode {
    USD(840), RUB(810), CNY(378);
    final int code;

    CurrencyCode(int code) {
        this.code = code;
    }
}
