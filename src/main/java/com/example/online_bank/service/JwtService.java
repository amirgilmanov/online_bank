package com.example.online_bank.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface JwtService {
    /**
     * @return Создать UUID для токена
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
     * Получаем роли из клаймов токена для spring authentification
     *
     * @param claims - клаймы токена
     * @return Роли пользователя
     */
    Collection<? extends GrantedAuthority> mapRolesForSpringToken(Claims claims);

    /**
     * Получить клайм "name"
     *
     * @param claims клаймы токена
     * @return Имя пользователя
     * <p>
     */
    String getUsername(Claims claims);

    /**
     * Получить subject(user.uuid)
     *
     * @param claims клаймы токена
     * @return uuid пользователя
     */
    String getSubject(Claims claims);

    String getId(Claims claims);
}
