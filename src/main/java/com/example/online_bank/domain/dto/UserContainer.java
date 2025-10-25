package com.example.online_bank.domain.dto;

import java.util.List;

public record UserContainer(
        String uuid, String name, List<String> roles
) {
}
