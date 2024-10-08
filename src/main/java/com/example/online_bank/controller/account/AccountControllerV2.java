package com.example.online_bank.controller.account;

import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.service.account.AccountServiceV2;
import com.example.online_bank.service.user.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v2/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Методы по работе со счетами пользователя")
public class AccountControllerV2 {
    private final AccountServiceV2 accountServiceV2;
    private final UserRegistrationService userRegistrationService;

    /**
     * @param token токен пользователя
     * @return возвращает список всех счетов пользователя
     */
    @GetMapping("/getAll")
    @Operation(summary = "Поиск счетов пользователя, по его токену")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountV2.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен счёт пользователя"),
            @ApiResponse(responseCode = "400", description = "Пользователь по данному токену не найден")
    })
    public ResponseEntity<List<AccountV2>> getAccounts(
            @Parameter(description = "Токен пользователя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token) {
        User user = userRegistrationService.findByToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(accountServiceV2.getAllAccounts(user));
    }

    /**
     * @param accountId номер счета
     * @return возвращает баланс пользователя по номеру счёта
     */
    @GetMapping(value = {"/{accountId}"})
    @Operation(summary = "Просмотреть баланс по номеру счёта")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = BigDecimal.class))
    )
    public ResponseEntity<BigDecimal> getBalance(
            @Parameter(description = "Номер счёта с валютным кодом", example = "840011067")
            @PathVariable(value = "accountId") String accountId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountServiceV2.getBalance(accountId));
    }

    /**
     * @param token токен пользователя
     * @return создаёт счёт для пользователя
     */
    @PostMapping()
    @Operation(summary = "Создать счёт для пользователя")
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    public ResponseEntity<String> createUserAccount(
            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token,
            @Parameter(description = "Код валюты", example = "USD")
            @RequestParam CurrencyCode currencyCode
    ) {
        User user = userRegistrationService.findByToken(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountServiceV2.createAccountForUser(user, currencyCode));
    }
}
