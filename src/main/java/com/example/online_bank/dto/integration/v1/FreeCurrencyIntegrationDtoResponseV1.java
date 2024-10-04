package com.example.online_bank.dto.integration.v1;

import java.math.BigDecimal;
import java.util.HashMap;


public record FreeCurrencyIntegrationDtoResponseV1(
        HashMap<String, BigDecimal> currencyResponse
) {
}
