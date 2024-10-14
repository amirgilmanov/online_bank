package com.example.online_bank.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.math.BigDecimal;

public record TransactionDtoResponse(
        @NonNull @Schema(description = "Количество денег к переводу", example = "100")
        BigDecimal amountMoney,
        @NonNull @Schema(description = "Описание к операции", example = "Оплата за услуги парикмахера")
        String description,
        @NonNull @Schema(description = "Имя отправителя", example = "Амир")
        String senderName,
        @NonNull @Schema(description = "Фамилия отправителя", example = "Гильманов")
        String senderSurname,
        @NonNull @Schema(description = "Отчество отправителя", example = "Отчествович")
        String senderPatronymic,
        @NonNull @Schema(description = "Номер счёта отправителя", example = "123456")
        String senderAccountId,
        @NonNull @Schema(description = "Имя банка", example = "EuroBank")
        String senderBankName,
        @NonNull @Schema(description = "Номер счёта получателя", example = "234567")
        String recipientAccountId
) {
}
