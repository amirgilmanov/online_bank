package com.example.online_bank.security.jwt.util;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.util.Base64;

@UtilityClass
public class SecretKeyEncoder {
    /**
     * {@summary Преобразуем байты SecretKey строку}
     * Encode делает строку из байтов {@code SecretKey}:
     * <p>
     * 1) {@code SecretKey.getBytes[] → String} - получаем байты SecretKey
     * <p>
     * 2) {@code .encodeToString(SecretKeyBytes)} - используя кодировку {@code ISO-8859-1} создаем строку из массива байтов
     */
    public static String encode(SecretKey secretKey) {
        // получаем байты секретного ключа
        byte[] secretKeyBytes = secretKey.getEncoded();
        return Base64
                //получаем вложенный Encoder из класса Base64  Base64.Encoder encoder = Base64.getEncoder();
                .getEncoder()
                //создаем String из массива байтов с помощью кодировки ISO-8859-1
                .encodeToString(secretKeyBytes);
    }
}
