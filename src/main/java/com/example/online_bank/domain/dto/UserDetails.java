package com.example.online_bank.domain.dto;

import java.util.Set;

public record UserDetails(
        String uuid, String name, Set<String> roles
) {
}
