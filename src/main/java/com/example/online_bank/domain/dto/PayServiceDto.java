package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.PartnerCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record PayServiceDto(
        UUID userUuid,
        String userAccountNumber,
        PartnerCategory category,
        BigDecimal amount,
        String partnerAccountNumber
) {
}
