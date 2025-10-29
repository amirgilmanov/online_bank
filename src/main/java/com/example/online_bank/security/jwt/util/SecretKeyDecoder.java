package com.example.online_bank.security.jwt.util;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.util.Base64;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

/**
 * Decode делает байты из строки
 * (то есть String → byte[], обратно из Base64.getDecoder)
 */
@UtilityClass
public class SecretKeyDecoder {
    /**
     * Декодирует строку во вновь выделенный массив байтов с использованием схемы кодирования Base64
     * и создает SecretKey на основе указанного массива байтов ключа
     *
     * @param base64SecretStr строка преобразованная из массива байтов
     * @return преобразованный SecretKey
     */
    public static SecretKey decode(String base64SecretStr) {
        byte[] decoded = Base64.getDecoder().decode(base64SecretStr); //преобразуем строку в массив байтов для SecretKey
        return hmacShaKeyFor(decoded); //Создает SecretKey по полученному массиву байтов
    }
}
