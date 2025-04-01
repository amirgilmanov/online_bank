package com.example.online_bank.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Класс пользователь с атрибутами: телефоном, фио, случайно сгенерированным UUID (UUID.randomUUID()).
 * Генерируется случайный пин-код.
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "user_bank")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String phoneNumber;

    @NonNull
    @Column
    private String name;

    @NonNull
    @Column
    private String surname;

    @Column
    private String patronymic;

    @Column
    private String token;

    @Column
    private String pinCode;

    @OneToMany(mappedBy = "holder", cascade = ALL)
    private List<Account> accounts;

    @OneToOne(mappedBy = "user",cascade = ALL)
    private AuthUsers authUsers;
}
