package com.example.online_bank.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * Класс пользователь с атрибутами: телефоном, фио, случайно сгенерированным UUID (UUID.randomUUID()).
 * Генерируется случайный пин-код.
 */
@AllArgsConstructor
@Builder
@Data
public class User {
    @NonNull
    private String phoneNumber;
    @NonNull
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String patronymic;
    private String token;
    private String pinCode;
}
