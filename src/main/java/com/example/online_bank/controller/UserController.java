package com.example.online_bank.controller;

import com.example.online_bank.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер для операций пользователя", description = "Методы для аутентификации и регистрации")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{phoneNumber}")
    public void deleteByPhoneNumber(@PathVariable String phoneNumber) {
        userService.deleteByPhoneNumber(phoneNumber);
    }
}
