package com.example.online_bank.service;

import com.example.online_bank.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final UserService userService;

    public String createAndSaveToken(User user) {
        log.info("Creating new token for user: {}", user);
        String token = "online%stoken".formatted(UUID.randomUUID());
        user.setToken(token);
        userService.save(user);
        return token;
    }
}
