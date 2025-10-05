package com.example.online_bank.security.jwt.util;

import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;

@UtilityClass
public class SecretKeyReader {

    public String readKeyFromFile(FileReader reader) throws IOException {
        var builder = new StringBuilder();
        try {

            int readSymbol;

            while ((readSymbol = reader.read()) != -1) {
                builder.append((char) readSymbol);
            }

            // log.debug("Возвращаю ключ");
            //TODO вынести в аспект
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
