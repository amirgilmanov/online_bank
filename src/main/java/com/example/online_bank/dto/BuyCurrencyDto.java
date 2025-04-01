package com.example.online_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO для транзакции с одного счета клиента на другой
 *
 * @param baseAccountNumber   Номер счёта с которого произойдет списание
 * @param targetAccountNumber Номер счёта к которому будет произведено пополнение
 * @param amount              Количество денег к переводу
 */
public record BuyCurrencyDto(
        @Schema(description = "Номер счёта с которого произойдет списание", example = "840")
        String baseAccountNumber,
        @Schema(description = "Номер счёта к которому будет произведено пополнение", example = "810")
        String targetAccountNumber,
        @Schema(description = "Количество денег к переводу", example = "100")
        BigDecimal amount
) {
}
