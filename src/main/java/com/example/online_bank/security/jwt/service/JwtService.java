package com.example.online_bank.security.jwt.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface JwtService {
    /**
     * @return Создание UUID для токена
     */
    String createUuid();

    /**
     * Создать клаймы для пользователя
     */
    Map<String, Object> createClaims();

    /**
     * @param token - JWT Access токен
     *              1) верифицируем токен через секретный ключ
     *              2) создаем jwt parser
     *              3)
     * @return получаем полезную нагрузку(клаймы из токена)
     */
    Claims getPayload(String token);

    /**
     * Получаем роли из клаймов токена для authentification
     *
     * @param claims - клаймы токена
     * @return Роли пользователя
     */
    Collection<? extends GrantedAuthority> getAuthoritiesForAuthToken(Claims claims);

    /**
     * @param claims клаймы токена
     * @return Имя пользователя
     * <p>
     * достаем name, что является именем пользователя
     */
    String getUsername(Claims claims);

    /**
     * @param claims клаймы токена
     * @return uuid пользователя
     */
    String getUuid(Claims claims);
}
