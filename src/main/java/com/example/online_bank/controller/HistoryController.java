package com.example.online_bank.controller;

import com.example.online_bank.dto.AccountDtoResponse;
import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.OperationInfoDtoResponse;
import com.example.online_bank.entity.Account;
import com.example.online_bank.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    /**
     * найти все операции по счету
     */
    @Operation(summary = "Найти все операции по номеру счета")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OperationDtoResponse.class)
            )
    )
    @GetMapping("/find-by-account-number")
    public List<OperationInfoDtoResponse> findByAccountNumber(
            @RequestParam String accountNumber,
            @RequestParam long page,
            @RequestParam long size) {
        return historyService.findOperationByAccountNumberPortion(accountNumber, page, size);
    }

    /**
     * Найти все операции пользователя(порционно)
     * </p>
     *
     * @param token токен пользователя
     * @return вернёт список всех операций для пользователя
     */
    @GetMapping("/find-all-operation-by-user")
    @Operation(summary = "Просмотреть список всех операций")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Operation.class))
    )
    public List<OperationInfoDtoResponse> getAllUserOperations(
            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token,

            @RequestParam
            @Parameter(description = "Страница начала показа операций", example = "5")
            int page,

            @RequestParam
            @Parameter(description = "Размер страницы", example = "10")
            int size
    ) {
        return historyService.showAllUserOperationHistory(token, page, size);
    }

    /**
     * Найти все счета пользователя
     *
     * @param token токен пользователя
     * @return возвращает список всех счетов пользователя
     */
    @GetMapping("/find-all-account-by-user")
    @Operation(summary = "Поиск всех счетов пользователя, по его токену")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен счёт пользователя"),
            //TODO 26.03.2025 проверить правильность http запроса
            @ApiResponse(responseCode = "400", description = "Пользователь по данному токену не найден")
    })
    public List<AccountDtoResponse> findAllAccountsByUser(
            @Parameter(
                    description = "Токен пользователя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token) {
        return historyService.findAllAccountsByUser(token);
    }
}
