package com.example.online_bank.controller;

import com.example.online_bank.dto.AccountDtoResponse;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.service.AccountService;
import com.example.online_bank.service.BankService;
import com.example.online_bank.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Методы по работе со счетами пользователя")
public class AccountController {
    private final BankService bankService;
    private final RegistrationService registrationService;
    private final AccountService accountService;

    /**
     * Создать счет для пользователя
     *
     * @param token        Токен пользователя
     * @param currencyCode Код валюты
     * @return Информацию об счете
     */
    @PostMapping()
    @Operation(summary = "Создать счёт для пользователя")
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
            )
    )
    public ResponseEntity<AccountDtoResponse> createUserAccount(
            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token,

            @Parameter(description = "Код валюты", example = "USD")
            @RequestParam CurrencyCode currencyCode
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.createAccount(token, currencyCode));
    }

    /**
     * Просмотреть баланс по счету
     *
     * @param accountNumber номер счета
     * @return возвращает баланс пользователя по номеру счёта
     */
    @GetMapping(value = {"/{accountNumber}"})
    @Operation(summary = "Просмотреть баланс по номеру счёта")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = BigDecimal.class)
            )
    )
    public BigDecimal getBalance(
            @Parameter(description = "Номер счёта с валютным кодом", example = "840011067")
            @PathVariable(value = "accountNumber") String accountNumber) {
        return accountService.getBalance(accountNumber);
    }
}
