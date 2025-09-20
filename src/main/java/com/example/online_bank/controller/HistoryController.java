package com.example.online_bank.controller;


import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.OperationInfoDto;
import com.example.online_bank.service.AccountService;
import com.example.online_bank.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {
    private final AccountService accountService;
    private final OperationService operationService;

    /**
     * Найти все операции по счету
     */
    @Operation(summary = "Найти все операции по номеру счета")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OperationDtoResponse.class)
            )
    )
    @GetMapping("/find-all-by-account-number")
    public List<OperationInfoDto> findByAccountNumber(
            @RequestParam String accountNumber,
            @RequestParam int page,
            @RequestParam int size) {
        return operationService.findAllByAccount(accountNumber, page, size);
    }

//    /**
//     * Найти все операции пользователя(порционно)
//     * </p>
//     *
//     * @param token токен пользователя
//     * @return вернёт список всех операций для пользователя
//     */
//    @GetMapping("/find-all-operation-by-user")
//    @Operation(summary = "Просмотреть список всех операций")
//    @ApiResponse(responseCode = "200",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = Operation.class))
//    )
//    public List<OperationInfoDto> getAllUserOperations(
//            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
//            @RequestHeader String token,
//
//            @RequestParam
//            @Parameter(description = "Страница начала показа операций", example = "5")
//            int page,
//
//            @RequestParam
//            @Parameter(description = "Размер страницы", example = "10")
//            int size
//    ) {
//        return operationService.findAllByUserPaged(token, page, size);
//    }

//    /**
//     * Найти все счета пользователя
//     *
//     * @param token токен пользователя
//     * @return возвращает список всех счетов пользователя
//     */
//    @GetMapping("/find-all-account-by-user")
//    @Operation(summary = "Поиск всех счетов пользователя, по его токену")
//    @ApiResponse(
//            responseCode = "200",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = Account.class)
//            )
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешно получен счёт пользователя"),
//            //TODO 26.03.2025 проверить правильность http запроса
//            @ApiResponse(responseCode = "400", description = "Пользователь по данному токену не найден")
//    })
//    public List<AccountDtoResponse> findAllAccountsByUser(
//            @Parameter(
//                    description = "Токен пользователя",
//                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
//            @RequestHeader String token) {
//        return accountService.findAllByHolder(token);
//    }
}
