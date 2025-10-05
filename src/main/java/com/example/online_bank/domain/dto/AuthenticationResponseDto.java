package com.example.online_bank.domain.dto;

import java.util.Set;

public record AuthenticationResponseDto(Set<String> tokens) {
}
