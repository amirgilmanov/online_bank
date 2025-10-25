package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.OtpType;

public record VerificationRequest(
        String contact, String code, OtpType type
) {
}
