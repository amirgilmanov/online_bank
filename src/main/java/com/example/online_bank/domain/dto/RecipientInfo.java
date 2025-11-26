package com.example.online_bank.domain.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @param accountNumberTo номер счета
 * @param bankName имя банка
 */
public record RecipientInfo(
        @Schema(description = "Номер счета получателя", example = "810000002")
        String accountNumberTo,
        @Parameter(description = "Имя банка", example = "EuroBank")
        String bankName
) {
}
