package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.AuthentificationRequest;
import com.example.online_bank.domain.dto.VerificationRequest;
import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //🔹 Первый вход
    //email
    //→ OTP
    //→ подтверждение OTP
    //→ create TrustedDevice
    //→ create TokenFamily
    //→ create RefreshToken
    //→ return access + refresh + deviceId

    /**
     * Верификация пользователя по электронной почте
     *
     * @return возвращает токен пользователя
     */
    @PostMapping("/first-auth-verify/email")
    @Operation(summary = "Верификация")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<AuthenticationResponseDto> authentication(@RequestBody VerificationRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(authenticationService.firstLogIn(dtoRequest));
    }

    @PostMapping("/silent")
    public ResponseEntity<AuthenticationResponseDto> silentLogin(@RequestHeader(name = "Refresh token") String refreshToken) {
        return ResponseEntity.status(200).body(authenticationService.silentLogin(refreshToken));
    }

    //если входим со старого/нового устройства и пароль с почтой верный, то добавляем устройство в семью токенов
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody AuthentificationRequest dto,
            @RequestHeader(name = "Device-Id")
            String deviceId,
            @RequestHeader(name = "Device-Name")
            String deviceName,
            @RequestHeader(name = "User-Agent")
            String userAgent) {
        authenticationService.login(dto.email(), dto.password(), deviceId, deviceName, userAgent);
        return ResponseEntity.ok().build();
    }
}
