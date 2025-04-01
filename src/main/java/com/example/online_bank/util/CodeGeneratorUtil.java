package com.example.online_bank.util;

import com.example.online_bank.enums.CurrencyCode;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class CodeGeneratorUtil {
    private final Random random = new Random();
    private static final int ACCOUNT_NUMBER_FORMAT = 100_000;
    private static final int USER_AUTH_CODE_FORMAT = 10_000;

    //Доп этап если ВСЕ сделано
    //Создание валютного отдела и валюты у счетов
    //1. Добавить к счетам валюту. В банке могут быть открыты счета в 3 валютах (Рубль, Юань, Доллар).
    //1.1. При генерации счетов добавляется в начале код валюты: рубль - 810, юань - 378, доллар 840.
    //Например:
    //было "000001"
    //Стало: "810000001"
    public static String generateAccountNumber(CurrencyCode currencyCode) {
        return String.format(currencyCode.getCode() + "%06d", random.nextInt(ACCOUNT_NUMBER_FORMAT));
    }

    public static String generatePinCode() {
        return String.format("%04d", random.nextInt(USER_AUTH_CODE_FORMAT));
    }

    @Deprecated
    public static String generateAccountNumber() {
        return String.format("%06d", random.nextInt(ACCOUNT_NUMBER_FORMAT));
    }
}
