package com.example.online_bank.security.jwt.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

@UtilityClass
@Slf4j
public class SecretKeyReader {

    /**
    * Считывает строку через файл
     */
    public String readKeyFromFile(FileReader reader) {
        var builder = new StringBuilder();
        try {
            int readSymbol;

            while ((readSymbol = reader.read()) != -1) {
                builder.append((char) readSymbol);
            }

            log.debug("Возвращаю ключ");
            reader.close();

        } catch (Exception e) {
            log.warn(e.getMessage());
            e.printStackTrace();
        }
        return builder.toString();
    }
}
