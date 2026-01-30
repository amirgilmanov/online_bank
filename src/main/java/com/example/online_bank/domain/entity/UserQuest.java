package com.example.online_bank.domain.entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность UserQuest (Связка и прогресс)
 * Эта таблица «раздает» квесты конкретным людям.
 */
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserQuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    @Column()
    private Boolean isComplete;
}
