//package com.example.online_bank.security.jwt.service.impl;
//
//import com.example.online_bank.security.jwt.JwtUtil;
//import com.example.online_bank.security.jwt.util.SecretKeyReader;
//import com.example.online_bank.security.jwt.util.SecretKeyWriter;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.security.Key;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.*;
//
//@Component
//@Slf4j
//public class JwtUtilImpl implements JwtUtil {
//    @Value("${jwt.lifetime}")
//    private Duration tokenLifetime;
//
//    @Value("${jwt.refresh-token-life-time}")
//    private Duration refreshTokenLifetime;
//
//    @Value("${jwt.not-before}")
//    private Duration notBeforeTime;
//
//    @Value("${jwt.secret-file-name}")
//    private String secretFileName;
//
//    @Value("${jwt.audience}")
//    private String audience;
//
//    @Value("${jwt.issuer}")
//    private String issuer;
//
//    private Key key;
//
//    @PostConstruct
//    public void init() throws IOException {
//        File file = new File(secretFileName);
//
//        if (file.exists()) {
//            String base64encodedKey = SecretKeyReader.readKeyFromFile(new FileReader(secretFileName));
//            SecretKey secretKey = SecretKeyManagerImpl.decodeSecretKey(base64encodedKey);
//            this.key = secretKey;
//        } else {
//            SecretKey secretKey = SecretKeyManagerImpl.createSecretKey();
//            this.key = secretKey;
//            String decodedSecretKey = SecretKeyManagerImpl.encodeSecretKey(secretKey);
//            SecretKeyWriter.writeKeyToFile(new FileWriter(file), decodedSecretKey);
//        }
//    }
//
//    public List<String> getUserAuthoritiesFromToken(String token) {
//        Claims claims = getPayload(token);
//        return claims.get("roles", List.class);
//    }
//
//    /**
//     * @param token - Информация о пользователе
//     * @return
//     */
//    @Override
//    public String generateAccessToken(Authentication token) {
//        Date issuedDate = new Date();
//        Date expiredDate = new Date(issuedDate.getTime() + tokenLifetime.toMillis());
//        Date notBeforeDate = new Date(issuedDate.getTime() + notBeforeTime.toMillis());
//        String subject = null;
//        if (token.getDetails() instanceof Map<?,?>) {
//            Map<String, Object> details = (Map<String, Object>)  token.getDetails();
//            String name = (String) details.get("name");
//            if (name != null && !name.isEmpty()) {
//                subject = name;
//            }
//        }
//
//        String jwtId = getJwtId();
//
//        List<String> userRoles = getUserAutritiez(token);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", userRoles);
//
//        return Jwts.builder()
//                .expiration(expiredDate)
//                .signWith(key)
//                .claims(claims)
//                .issuedAt(issuedDate)
//                .subject(subject)
//                .notBefore(notBeforeDate)
//                .id(jwtId)
//                .audience().add(audience)
//                .and()
//                .issuer(issuer)
//                .compact();
//    }
//
//    private String getJwtId() {
//        return UUID.randomUUID().toString();
//    }
//
//    /**
//     * @param token - Информация о пользователе
//     * @return Refresh токен
//     */
//    //TODO реализовать в будущем с помощью redis таблицу с удаленными/заблокирвоанными токенами
//    @Override
//    public String generateRefreshToken(Authentication token) {
//        Date issuedDate = new Date();
//        Date notBeforeDate = Date.from(Instant.now());
//        Date expiredAt = new Date(issuedDate.getTime() + refreshTokenLifetime.toMillis());
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("access_token", generateAccessToken(token));
//
//        return Jwts.builder()
//                .issuer(issuer)
//                .id(getJwtId())
//                .notBefore(notBeforeDate)
//                .expiration(expiredAt)
//                .signWith(key)
//                .audience().add(audience)
//                .and()
//                .claims(claims)
//                .compact();
//    }
//
//    /**
//     * @param token - token - Информация о пользователе
//     * @return Токен Id
//     */
//    //TODO: добавить фотографию профиля пользователю и подгружать через Amazon S3
//    @Override
//    public String generateIdToken(Authentication token) {
//        String accessToken = generateAccessToken(token);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("access_token", accessToken);
//
//        return Jwts.builder()
//                // .claims(claims)
//                .audience().add(audience)
//                .and()
//                .issuer(issuer)
//                .signWith(key)
//                .compact();
//    }
//
//    public String getUserName(String token) {
//        return getPayload(token).getSubject();
//    }
//
//    /**
//     * @param token - JWT Access токен
//     * @return Клаймы токена
//     */
//    @Override
//    public Claims getPayload(String token) {
//        return Jwts.parser()
//                .verifyWith((SecretKey) key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    /**
//     * @param authentication - Информация о пользователе
//     * @return Роли пользователя
//     */
//    @Override
//    public List<String> getUserAutritiez(Authentication authentication) {
//        return authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//    }
//}