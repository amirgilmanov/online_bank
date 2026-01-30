package com.example.online_bank.domain.dto;

import com.example.online_bank.enums.CurrencyCode;

public record ServiceInfo(
        CurrencyCode accountCurrencyCode,
        String partnerName
) {
}
