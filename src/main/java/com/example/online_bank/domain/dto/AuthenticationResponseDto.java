package com.example.online_bank.domain.dto;

public record AuthenticationResponseDto(String accessToken, String idToken, String refreshToken) {
}
