package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.ContentType;

public record VerificationRequest(
        String contact, String code, ContentType type
) {
}
