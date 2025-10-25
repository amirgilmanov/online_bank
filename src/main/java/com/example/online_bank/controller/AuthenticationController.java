package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.AuthenticationRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Аутентификация пользователя по электронной почте
     *
     * @return возвращает токен пользователя
     */
    @PostMapping("/email")
    @Operation(summary = "Аутентификация")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<AuthenticationResponseDto> authentication(@RequestBody AuthenticationRequest dtoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body( authenticationService.signIn(dtoRequest));
    }
}
