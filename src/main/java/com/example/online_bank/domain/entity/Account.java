package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

/**
 * Номер счета (уникален), владелец (класс Пользователь этап1 пункт3), остаток на счете (с копейками).
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal balance;

    @Column
    private String accountNumber;

    @Column
    @Enumerated(STRING)
    private CurrencyCode currencyCode;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Operation> operations;

    @Column()
    private Boolean isBlocked;

    @ManyToOne()
    @JoinColumn(name = "holder_id", referencedColumnName = "id")
    @ToString.Exclude
    private User holder;
}
