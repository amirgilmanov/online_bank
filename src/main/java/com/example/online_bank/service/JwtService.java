package com.example.online_bank.service;

import com.example.online_bank.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final JwtConfig jwtConfig;

    /**
     * @return Создать UUID для токена
     */
    public String createUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Создать клаймы для пользователя
     */
    public Map<String, Object> createClaims() {
        return new HashMap<>();
    }

    /**
     * @param token - JWT Access токен
     *              1) верифицируем токен через секретный ключ
     *              2) создаем jwt parser
     *              3)
     * @return получаем полезную нагрузку(клаймы из токена)
     */
    public Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtConfig.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JwtException(e.getMessage());
        }

    }

    /**
     * Получаем роли из клаймов токена для spring authentification
     *
     * @param claims - клаймы токена
     * @return Роли пользователя
     */
    public Collection<? extends GrantedAuthority> mapRolesForSpringToken(Claims claims) {
        return claims.get("roles", List.class).stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .toList();
    }

    /**
     * Получить клайм "name"
     *
     * @param claims клаймы токена
     * @return Имя пользователя
     * <p>
     */
    public String getUsername(Claims claims) {
        return claims.get("name", String.class);
    }

    /**
     * Получить subject(user.uuid)
     *
     * @param claims клаймы токена
     * @return uuid пользователя
     */
    public String getSubject(Claims claims) {
        return claims.getSubject();
    }

    public String getId(Claims claims) {
        return claims.getId();
    }

    public String getJwtTokenUuid(String token) {
        try {
            Claims payload = getPayload(token);
            return getId(payload);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public void validateToken(String refreshToken) {
        try {
            getPayload(refreshToken);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверный или просроченный токен");
        }
    }
}
