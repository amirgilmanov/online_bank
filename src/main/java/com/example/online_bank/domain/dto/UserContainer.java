package com.example.online_bank.domain.dto;

import java.util.List;

/**
 * Контейнер для хранения данных пользователя
 *
 * @param uuid uuid пользователя
 * @param name имя пользователя
 * @param roles роли пользователя
 */
public record UserContainer(
        String uuid, String name, List<String> roles
) {
}
