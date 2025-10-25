package com.example.online_bank.security.jwt.factory.impl;

import com.example.online_bank.config.JwtConfig;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.enums.TokenType;
import com.example.online_bank.security.jwt.factory.TokenFactory;
import com.example.online_bank.security.jwt.service.JwtService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.online_bank.enums.TokenType.ACCESS;

@RequiredArgsConstructor
public class AccessTokenFactory implements TokenFactory {
    private final JwtConfig config;
    private final JwtService jwtService;

    /**
     * Создает access токен.
     * Проверяет тип токена.
     * Создает даты
     */
    @Override
    public String createToken(TokenType type, UserContainer userContainer) {
        if (!supports(type)) {
            throw new IllegalArgumentException("Unsupported token type: " + type);
        }

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + config.getAccessTokenLifetime().toMillis());
        Date notBeforeDate = new Date(issuedDate.getTime() + config.getNotBeforeTime().toMillis());

        String subject = userContainer.uuid();
        List<String> roles = userContainer.roles();

        String id = jwtService.createUuid();

        Map<String, Object> claims = jwtService.createClaims();
        claims.put("roles", roles);
        claims.put("token_type", type);

        return Jwts.builder()
                .expiration(expiredDate)
                .signWith(config.getKey())
                .claims(claims)
                .issuedAt(issuedDate)
                .subject(subject)
                .notBefore(notBeforeDate)
                .id(id)
                .audience().add(config.getAudience())
                .and()
                .issuer(config.getIssuer())
                .compact();
    }

    /**
     * @param supported
     * @return
     */
    @Override
    public boolean supports(TokenType supported) {
        return supported.equals(ACCESS);
    }
}
