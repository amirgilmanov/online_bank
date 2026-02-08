package com.example.online_bank.domain.event;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.PartnerCategory;

import java.time.LocalDate;

public record UpdateUserQuestEvent(
        Integer userProgress,
        PartnerCategory category,
        User user,
        LocalDate spendPeriod,
        String userAccountNumber
) {
}
