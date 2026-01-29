package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.PartnerCategory;

public record BankPartnerDto(
        String name,
        PartnerCategory category
) {
}
