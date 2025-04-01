package com.example.online_bank.controller;

import com.example.online_bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign-in")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации")
public class AuthController {

    private final AuthService authService;

    /**
     * Аутентификация пользователя
     *
     * @param phoneNumber номер телефона пользователя
     * @param pinCode     пин-код пользователя
     * @return возвращает токен пользователя
     */
    @PostMapping()
    @Operation(summary = "Аутентификация")
    @ApiResponse(responseCode = "201",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
    )
    public String authentication(
            @Parameter(description = "Номер телефона", example = "+7992281488")
            @RequestParam String phoneNumber,
            @Parameter(description = "Пин-код", example = "1753")
            @RequestParam String pinCode) {
        return authService.signIn(phoneNumber, pinCode);
    }
}
