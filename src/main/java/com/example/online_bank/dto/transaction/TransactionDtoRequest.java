package com.example.online_bank.dto.transaction;

import com.example.online_bank.enums.CurrencyCode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * @param amount              количество денег
 * @param description         описание
 * @param name                имя отправителя
 * @param surname             фамилия отправителя
 * @param patronymic          отчество отправителя
 * @param paymentCurrencyCode код валюты
 * @param bankName            Имя банка
 */

public record TransactionDtoRequest(
        @Schema(description = "Количество денег к переводу", example = "100")
        BigDecimal amount,
        @Schema(description = "Описание к операции", example = "Привет, скинул на пикник")
        String description,
        @Schema(description = "Имя отправителя", example = "Амир")
        String name,
        @Schema(description = "Фамилия отправителя", example = "Гильманов")
        String surname,
        @Schema(description = "Отчество отправителя", example = "Отчествович")
        String patronymic,
        @Schema(description = "Код валюты", example = "USD, EUR")
        CurrencyCode paymentCurrencyCode,
        @Parameter(description = "Имя банка", example = "EuroBank")
        String bankName
) {
}
