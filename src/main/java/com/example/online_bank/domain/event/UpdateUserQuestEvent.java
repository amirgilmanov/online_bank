package com.example.online_bank.domain.event;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.PartnerCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateUserQuestEvent(
        BigDecimal totalSpend,
        Integer userProgress,
        PartnerCategory category,
        User user,
        LocalDateTime spendPeriod
) {
}
