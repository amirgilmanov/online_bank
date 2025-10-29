package com.example.online_bank.security.jwt.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@UtilityClass
public class SecretKeyWriter {
    /**
     * Записывает закодированные байты ключа в файл
     */
    public void writeKeyToFile(FileWriter writer, String encodedSecretKeyBytes) {
        try {
            log.debug("Записываю ключ в файл");
            writer.write(encodedSecretKeyBytes);
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
