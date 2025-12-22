package com.example.online_bank.domain.dto;

import java.util.Map;

public record AuthenticationResponseDto(Map<String, String> tokens) {
}
