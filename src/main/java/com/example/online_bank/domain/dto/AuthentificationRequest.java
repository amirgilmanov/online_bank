package com.example.online_bank.domain.dto;

public record AuthentificationRequest(
        String email, String password
) {
}
