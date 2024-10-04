package com.example.online_bank.controller;

import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.finance_dto.DepositMoneyDtoRequest;
import com.example.online_bank.dto.finance_dto.WithdrawMoneyDtoRequest;
import com.example.online_bank.entity.Operation;
import com.example.online_bank.service.bank.BankServiceV2;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
@Tag(name = "Контроллер финансовых операций", description = "Методы финансовых операций внутри банка")
public class OperationController {
    private final BankServiceV2 bankServiceV2;

    /**
     * @param token токен пользователя
     * @return вернёт список всех операций для пользователя
     */
    @GetMapping("/showAll")
    @io.swagger.v3.oas.annotations.Operation(summary = "Просмотреть список всех операций")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)))
    public ResponseEntity<List<Operation>> getAllUserOperations(
            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(bankServiceV2.showAllUserHistory(token));
    }

    /**
     * @param dto       количество денег для платежа по пользовательскому счету, описание к операции
     * @param token     токен пользователя
     * @param accountId номер счета пользователя
     * @return делает платеж по пользовательскому счёту
     */
    @PostMapping("/pay")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Сделать платеж по пользовательскому счёту/Списать деньги с пользовательского счёта"
    )
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "text/plain")
    )
    public ResponseEntity<OperationDtoResponse> withdrawMoney(
            @RequestBody WithdrawMoneyDtoRequest dto,
            @Parameter(description = "Токен пользователя", example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token,
            @Parameter(description = "Номер счёта с которого произойдёт списание")
            @RequestHeader String accountId
    ) {
        bankServiceV2.makePayment(
                accountId,
                dto.amountMoney(),
                dto.description(),
                token,
                dto.depositCurrencyCode()
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(bankServiceV2.makePayment(
                accountId,
                dto.amountMoney(),
                dto.description(),
                token,
                dto.depositCurrencyCode()
        ));
    }

    /**
     * @param dto                количество денег для платежа по пользовательскому счету, описание к операции
     * @param recipientAccountId номер счета пользователя куда придут деньги
     * @return делает зачисление по этому счету.
     */
    @PostMapping("/receive")
    @io.swagger.v3.oas.annotations.Operation(summary = "Пополнить счёт пользователя по номеру счёта")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)))
    public ResponseEntity<OperationDtoResponse> receiveMoney(
            @RequestBody DepositMoneyDtoRequest dto,
            @Parameter(description = "Номер счёта к которому произойдёт пополнение", example = "087123")
            @RequestHeader String recipientAccountId,
            @Parameter(description = "Токен пользователя для проверки",
                    example = "online9c00fb59-420f-4c41-9bf1-7f5239db4cb0token")
            @RequestHeader String token
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(bankServiceV2.depositMoney(
                recipientAccountId,
                dto.amountMoney(),
                token,
                dto.description(),
                dto.depositCurrencyCode()
        ));
    }

//    @PostMapping("/transfer")
//    public ResponseEntity<?> transferMoney(
//            @RequestParam String senderAccountId,
//            @RequestParam String recipientAccountId,
//            @RequestBody TransactionDtoRequest dto
//    ) {
//        bankService.transferToAnotherBank(senderAccountId, recipientAccountId, dto);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
//    }
}
