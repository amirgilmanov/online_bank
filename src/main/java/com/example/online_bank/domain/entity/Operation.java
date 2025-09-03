package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;


/**
 * Сущность операция - уникальный идентификатор, дата+время (равна времени создания операции)
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Operation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdAt;

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

    @JoinColumn(name = "account_id")
    @ManyToOne
    @ToString.Exclude
    private Account account;
}
