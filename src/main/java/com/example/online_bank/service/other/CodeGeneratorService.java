package com.example.online_bank.service.other;

import com.example.online_bank.enums.CurrencyCode;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorService {
    private final Random random = new Random();
    private static final int ACCOUNT_ID_CODE_FORMAT = 100_000;
    private static final int USER_AUTH_CODE_FORMAT = 10_000;

    public String generatedAccountId() {
        return String.format("%06d", random.nextInt(ACCOUNT_ID_CODE_FORMAT));
    }

    //Доп этап если ВСЕ сделано
    //Создание валютного отдела и валюты у счетов
    //1. Добавить к счетам валюту. В банке могут быть открыты счета в 3 валютах (Рубль, Юань, Доллар).
    //1.1. При генерации счетов добавляется в начале код валюты: рубль - 810, юань - 378, доллар 840.
    //Например:
    //было "000001"
    //Стало: "810000001"332123
    public String generatedAccountIdV2(CurrencyCode currencyCode) {
        switch (currencyCode) {
            case USD -> {
                return String.format(CurrencyCode.USD.getCode() + "%06d", random.nextInt(ACCOUNT_ID_CODE_FORMAT));
            }
            case RUB -> {
                return String.format(CurrencyCode.RUB.getCode() + "%06d", random.nextInt(ACCOUNT_ID_CODE_FORMAT));
            }
            case CNY -> {
                return String.format(CurrencyCode.CNY.getCode() + "%06d", random.nextInt(ACCOUNT_ID_CODE_FORMAT));
            }
            default -> {
                return String.format("%06d", random.nextInt(ACCOUNT_ID_CODE_FORMAT));
            }
        }
    }

    public String generatePinCode() {
        return String.format("%04d", random.nextInt(USER_AUTH_CODE_FORMAT));
    }
}
