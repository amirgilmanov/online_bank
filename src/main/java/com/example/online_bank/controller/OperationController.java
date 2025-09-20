package com.example.online_bank.controller;

import com.example.online_bank.service.BankService;
import com.example.online_bank.service.OperationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation")
@RequiredArgsConstructor
@Tag(name = "Контроллер финансовых операций", description = "Методы финансовых операций внутри банка")
public class OperationController {
    private final BankService bankService;
    private final OperationService operationService;

//    /**
//     * Снять деньги с банкомата(списать)
//     *
//     * @param dto   количество денег для платежа по пользовательскому счету, описание к операции
//     * @param token токен пользователя
//     * @return делает платеж по пользовательскому счёту
//     */
//    @PostMapping("/pay")
//    @Operation(summary = "Сделать платеж по пользовательскому счёту/Списать деньги с пользовательского счёта")
//    @ApiResponse(
//            responseCode = "202",
//            content = @Content(mediaType = "text/plain")
//    )
//    public OperationDtoResponse withdrawMoney(
//            @RequestBody FinanceOperationDto dto,
//
//            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
//            @RequestHeader String token
//    ) {
//        return bankService.withdraw(token, dto);
//    }

//    /**
//     * Пополнить счет
//     *
//     * @param dto количество денег для пополнения по пользовательскому счету, описание к операции
//     * @return делает зачисление по этому счету.
//     */
//    @PostMapping("/receive")
//    @Operation(summary = "Пополнить счёт пользователя по номеру счёта")
//    @ApiResponse(
//            responseCode = "200",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = OperationDtoResponse.class)
//            )
//    )
//    public OperationDtoResponse receive(
//            @RequestBody FinanceOperationDto dto,
//
//            @Parameter(
//                    description = "Токен пользователя для проверки",
//                    example = "online9c00fb59-420f-4c41-9bf1-7f5239db4cb0token"
//            )
//            @RequestHeader String token
//    ) {
//        return bankService.deposit(token, dto);
//    }
}
