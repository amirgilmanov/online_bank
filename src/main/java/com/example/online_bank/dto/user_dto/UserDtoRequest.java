package com.example.online_bank.dto.user_dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserDtoRequest(
        @Schema(description = "Номер телефона", example = "+79992281488")
        String phone,
        @Schema(description = "Имя пользователя", example = "Амир")
        String name,
        @Schema(description = "Фамилия пользователя", example = "Гильманов")
        String surname,
        @Schema(description = "Отчество пользователя", example = "Отчествович")
        String patronymic
) {
}
