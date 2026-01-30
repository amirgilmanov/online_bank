package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.PartnerCategory;

import java.math.BigDecimal;

public record PayDtoRequest(
        SenderInfo senderInfo,
        ServiceInfo serviceInfo,
        BigDecimal serviceRequestAmount,
        PartnerCategory category
) {
}
