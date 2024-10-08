package com.example.online_bank.controller.user;

import com.example.online_bank.dto.user_dto.UserDtoRequest;
import com.example.online_bank.service.user.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("checkstyle:RegexpSingleline")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер для операций пользователя", description = "Методы для аутентификации и регистрации")
public class UserController {
    private final UserRegistrationService userRegistrationService;

    /**
     * Регистрация пользователя
     *
     * @param userDtoRequest телефон, ФИО владельца
     * @return пин-код для аутентификации
     */
    @PostMapping("/signup")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "201",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<String> signUP(@RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Ваш пин-код для аутентификации: " + userRegistrationService.registration(userDtoRequest));
    }

    /**
     * Аутентификация пользователя
     *
     * @param phoneNumber номер телефона пользователя
     * @param pinCode     пин-код пользователя
     * @return возвращает токен пользователя
     */
    @PostMapping("/auth")
    @Operation(summary = "Аутентификация")
    @ApiResponse(responseCode = "201",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<String> authentication(
            @Parameter(description = "Номер телефона", example = "+79608052797")
            @RequestParam String phoneNumber,
            @Parameter(description = "Пин-код", example = "1488")
            @RequestParam String pinCode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Ваш токен: " + userRegistrationService.authenticationUser(phoneNumber, pinCode));
    }

    /**
     * Получение всех пользователй
     *
     * @return список всех созданных пользователей
     */
    @GetMapping("/getAll")
    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
    )
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRegistrationService.getAll());
    }
}
