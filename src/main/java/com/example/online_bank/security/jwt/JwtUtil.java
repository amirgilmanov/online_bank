package com.example.online_bank.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface JwtUtil {
    String generateAccessToken(Authentication token);

    String generateRefreshToken(Authentication token);

    String generateIdToken(Authentication token);

    List<String> getUserAutritiez(Authentication token);

    Claims getPayload(String token);
}