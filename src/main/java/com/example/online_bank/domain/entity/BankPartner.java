package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.PartnerCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BankPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private PartnerCategory partnerCategory;

    @JoinColumn(name = "account_id")
    @OneToOne
    private Account account;
}
