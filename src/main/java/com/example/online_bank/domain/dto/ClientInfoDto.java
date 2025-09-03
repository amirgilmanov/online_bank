package com.example.online_bank.domain.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Информация о клиенте - ФИО, номер счета, название банка
 *
 * @param name          Имя клиента
 * @param surname       Фамилия клиента
 * @param patronymic    Отчество клиента(может быть null)
 * @param accountNumber Номер счета клиента
 * @param bankName      Имя банка
 */
public record ClientInfoDto(
        @Schema(description = "Имя отправителя", example = "Амир")
        String name,

        @Schema(description = "Фамилия отправителя", example = "Гильманов")
        String surname,

        @Schema(description = "Отчество отправителя", example = "Отчествович")
        String patronymic,

        @Schema(description = "Номер счета", example = "810000001")
        String accountNumber,

        @Parameter(description = "Имя банка", example = "EuroBank")
        String bankName
) {
}
