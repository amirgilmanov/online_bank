package com.example.online_bank.security.jwt.service.impl;

import com.example.online_bank.security.jwt.service.SecretKeyDecoder;
import com.example.online_bank.security.jwt.service.SecretKeyEncoder;
import com.example.online_bank.security.jwt.util.SecretKeyReader;
import com.example.online_bank.security.jwt.util.SecretKeyWriter;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecretKeyManagerImpl {
    private final SecretKeyDecoder decoder;
    private final SecretKeyEncoder encoder;

    public SecretKey readAndDecodeKey(String fileName) throws IOException {
        String base64encodedKey = SecretKeyReader.readKeyFromFile(new FileReader(fileName));
        return decoder.decode(base64encodedKey);
    }

    public void encodeAndWriteKey(FileWriter writer, SecretKey secretKey) throws IOException {
        String encoded = encoder.encode(secretKey);
        SecretKeyWriter.writeKeyToFile(writer, encoded);
    }

    public SecretKey createSecretKey() {
        return Jwts
                .SIG
                .HS256
                .key()
                .build(); // генерируем секретный ключ
    }
}
