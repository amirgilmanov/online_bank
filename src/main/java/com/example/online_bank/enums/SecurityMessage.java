package com.example.online_bank.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum SecurityMessage {
    SECURITY_MESSAGE("Обнаружена попытка взлома! Рекомендуем срочно сменить пароли"),
    CONFIRM_LOGIN_MESSAGE("Подтвердите вход с нового устройства, введя проверочный код");

    private final String value;
}
