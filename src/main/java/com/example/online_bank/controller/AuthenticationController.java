package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.LoginRequestDto;
import com.example.online_bank.domain.dto.RefreshTokenRequestDto;
import com.example.online_bank.domain.dto.VerificationRequest;
import com.example.online_bank.service.AuthenticationService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

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
    public ResponseEntity<AuthenticationResponseDto> silentLogin(@RequestBody RefreshTokenRequestDto dto) {
        return ResponseEntity.status(200).body(authenticationService.silentLogin(dto.token()));
    }

    //если входим со старого/нового устройства и пароль с почтой верный, то добавляем устройство в семью токенов
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto dto) {
        authenticationService.login(dto.email(), dto.password(), dto.deviceId(), dto.deviceName(), dto.userAgent());
        return ResponseEntity.status(200).body(authenticationService.login(
                dto.email(),
                dto.password(),
                dto.deviceId(),
                dto.deviceName(),
                dto.userAgent()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequestDto dto) {
        authenticationService.logout(dto.token());
        return ResponseEntity.ok().build();
    }
}
