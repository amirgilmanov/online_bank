package com.example.online_bank.domain.entity;

import java.time.LocalDateTime;

public class Quest {
    private Long id;
    private LocalDateTime dateOfIssue;
    private LocalDateTime dateOfExpiry;
    private Boolean isArchived;
    private Integer pointAmount;
    private Account account;
}
