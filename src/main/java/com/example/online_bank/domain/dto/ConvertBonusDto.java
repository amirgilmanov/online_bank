package com.example.online_bank.domain.dto;

import java.math.BigDecimal;

public record ConvertBonusDto(
        String accountNumber,
        BigDecimal points
) {
}
