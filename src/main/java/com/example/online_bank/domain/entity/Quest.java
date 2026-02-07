package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.PartnerCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PartnerCategory category;
    @Column
    private LocalDate dateOfExpiry;
    @Column
    private Integer pointReward;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quest")
    @ToString.Exclude
    private List<UserQuest> userQuest;

    @Column
    private Integer progress;

}
