package com.example.online_bank.security.jwt.factory.impl;

import com.example.online_bank.config.JwtConfig;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.security.jwt.factory.TokenFactory;
import com.example.online_bank.security.jwt.service.SpringAuthenticationService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class IdTokenFactory implements TokenFactory {
    private final JwtConfig config;
    private final SpringAuthenticationService springAuthenticationService;
    /**
     * @param auth - token - Информация о пользователе
     * @return Токен Id
     */
    //TODO: добавить фотографию профиля пользователю и подгружать через Amazon S3
    @Override
    public String createToken(TokenType type, Authentication auth) {
        if (!supports(type)) {
            throw new IllegalArgumentException("Unsupported token type: " + type);
        }

        Map<String, Object> details = springAuthenticationService.getDetails(auth);
        String name = springAuthenticationService.getName(details);

        return Jwts.builder()
                .subject(name)
                .audience().add(config.getAudience())
                .and()
                .issuer(config.getIssuer())
                .signWith(config.getKey())
                .compact();
    }


    /**
     * @param supported
     * @return
     */
    @Override
    public boolean supports(TokenType supported) {
        return supported.equals(TokenType.ID);
    }
}
