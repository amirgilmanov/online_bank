package com.example.online_bank.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для перевода средств между клиентами.
 * Имя банка указывается у каждого клиента отдельно
 *
 * @param senderInfoDto    Информация об отправителе (Имя, фамилия, отчество(при наличии), номер счета, имя банка)
 * @param recipientInfoDto Информация об получателе (Имя, фамилия, отчество(при наличии), номер счета, имя банка)
 * @param amount           Количество денег
 * @param description      Описание к операции
 * @param operationTime    Время операции
 */
public record TransactionDto(
        ClientInfoDto senderInfoDto,
        ClientInfoDto recipientInfoDto,
        BigDecimal amount,
        String description,
        LocalDateTime operationTime
) {
}
