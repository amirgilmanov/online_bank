package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign-up")
@RequiredArgsConstructor
@Tag(name = "Контроллер регистрации")
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * Регистрация пользователя
     *
     * @param registrationDto телефон, ФИО владельца
     * @return пин-код для аутентификации
     */
    @PostMapping()
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<String> signUp(@RequestBody RegistrationDto registrationDto) {
        registrationService.signUp(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
