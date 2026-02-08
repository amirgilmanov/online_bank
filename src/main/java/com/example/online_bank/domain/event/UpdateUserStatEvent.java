package com.example.online_bank.domain.event;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.PartnerCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateUserStatEvent(
        User user,
        PartnerCategory partnerCategory,
        BigDecimal spendAmount,
        LocalDate operationDate,
        String userAccount

) {
}
