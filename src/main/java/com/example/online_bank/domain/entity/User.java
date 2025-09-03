package com.example.online_bank.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Класс пользователь с атрибутами: телефоном, фио, случайно сгенерированным UUID (UUID.randomUUID()).
 * Генерируется случайный пин-код.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_bank")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private UUID uuid;

    @Column
    private String passwordHash;

    @Column()
    private Integer failedAttempts;

    @Column
    private Boolean isBlocked;

    @Column
    private LocalDateTime blockedExpiredAt;

    @Column(unique = true)
    @Email
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String patronymic;

    @Column
    private Boolean isVerified;

    @OneToMany(mappedBy = "holder", cascade = ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Account> accounts;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<VerifiedCode> verifiedCode;

    @ManyToMany()
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private List<Role> roles;
}
