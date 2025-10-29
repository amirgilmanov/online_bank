package com.example.online_bank.security.jwt.service.impl;

import com.example.online_bank.config.JwtConfig;
import com.example.online_bank.security.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;

    @Override
    public String createUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Map<String, Object> createClaims() {
        return new HashMap<>();
    }

    @Override
    public Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Collection<? extends GrantedAuthority> mapRolesForSpringToken(Claims claims) {
        return claims.get("roles", List.class).stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .toList();
    }

    @Override
    public String getUsername(Claims claims) {
        return claims.get("name", String.class);
    }

    @Override
    public String getSubject(Claims claims) {
        return claims.getSubject();
    }
}
