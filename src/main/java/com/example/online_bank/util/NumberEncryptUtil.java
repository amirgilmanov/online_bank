package com.example.online_bank.util;

import jakarta.validation.constraints.Size;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberEncryptUtil {
    private static final String ENCRYPT_PATTERN = "*******";

    public static String encryptPhoneNumber(@Size(max = 11) String password) {
        String showPart = password.substring(7, 11);
        return ENCRYPT_PATTERN + showPart;
    }
}
