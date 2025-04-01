package com.example.online_bank.entity;

import com.example.online_bank.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static jakarta.persistence.EnumType.ORDINAL;

/**
 * Номер счета (уникален), владелец (класс Пользователь этап1 пункт3), остаток на счете (с копейками).
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column
    private String accountNumber;

    @Column
    @Enumerated(ORDINAL)
    private CurrencyCode currencyCode;

    @OneToMany(mappedBy = "account")
    private List<Operation> operations;

    @ManyToOne()
    @JoinColumn(name = "holder_id", referencedColumnName = "id")
    @ToString.Exclude
    private User holder;
}
