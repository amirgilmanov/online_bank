package com.example.online_bank.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SenderInfo(
        @Schema(description = "Имя отправителя", example = "Амир")
        String name,

        @Schema(description = "Фамилия отправителя", example = "Гильманов")
        String surname,

        @Schema(description = "Отчество отправителя", example = "Азатович")
        String patronymic,

        @Schema(description = "Номер счета отправителя", example = "810000001")
        String accountNumberFrom
) {
}
