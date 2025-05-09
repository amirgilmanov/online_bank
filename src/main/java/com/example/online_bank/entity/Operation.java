package com.example.online_bank.entity;

import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


/**
 * Сущность операция - уникальный идентификатор, дата+время (равна времени создания операции)
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @JoinColumn(name = "account_id")
    @ManyToOne
    @ToString.Exclude
    private Account account;

    @Column
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column
    private BigDecimal amount;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;
}
