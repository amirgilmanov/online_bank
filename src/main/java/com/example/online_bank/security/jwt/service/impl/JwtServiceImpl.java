package com.example.online_bank.security.jwt.service.impl;

import com.example.online_bank.config.JwtConfig;
import com.example.online_bank.security.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;

    /**
     * @param token токен
     * @return Имя пользователя
     * <p>
     * Получаем Payload(claims) и оттуда достаем subject, что является именем пользователя
     */
    @Override
    public String getUsername(String token) {
        return getPayload(token).getSubject();
    }

    /**
     * @return Создание UUID для токена
     */
    @Override
    public String createId() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param token - JWT Access токен
     *              1) верифицируем токен через секретный ключ
     *              2) создаем jwt parser
     *              3)
     * @return получаем полезную нагрузку(клаймы из токена)
     */
    @Override
    public Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * @param authentication - Информация о пользователе
     * @return Роли пользователя
     */
    @Override
    public List<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    /**
     * Достаем роли из клаймов "roles"
     */
    @Override
    public List<String> getUserAuthoritiesFromClaims(String token) {
        Claims claims = getPayload(token);
        return claims.get("roles", List.class);
    }

    public Map<String, Object> createClaims() {
        return new HashMap<>();
    }

}
