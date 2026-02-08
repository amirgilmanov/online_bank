package com.example.online_bank.domain.event;

public record UpdateBonusAccountEvent(
        Integer points,
        String accountNumber
) {
}
