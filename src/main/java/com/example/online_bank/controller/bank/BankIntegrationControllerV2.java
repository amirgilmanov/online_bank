package com.example.online_bank.controller.bank;

import com.example.online_bank.dto.transaction.BuyCurrencyDtoRequest;
import com.example.online_bank.dto.transaction.TransactionDtoRequest;
import com.example.online_bank.dto.transaction.TransactionDtoResponse;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.service.bank.BankServiceV2;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v2/bank")
@RequiredArgsConstructor
@Tag(name = "Интеграция банковV2", description = "Методы по рабе интеграции между банками")
public class BankIntegrationControllerV2 {
    private final BankServiceV2 bankServiceV2;

    @GetMapping("/info")
    @Operation(summary = "Получить название текущего банка")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = String.class, example = "EuroBank")))
    public ResponseEntity<String> info() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bankServiceV2.info());
    }

    @Operation(summary = "Сделать перевод в другой банк")
    @PostMapping("/transferToPartner")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionDtoResponse.class))
    )
    public ResponseEntity<TransactionDtoResponse> receiveTransferMoneyToPartnerBank(

            @RequestBody TransactionDtoRequest dto,
            @Parameter(description = "Номер счёта отправителя", example = "123456")
            @RequestHeader String senderAccountId,
            @Parameter(description = "Номер счёта получателя", example = "123456")
            @RequestHeader String recipientAccountId,
            @Parameter(description = "Токен отправителя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String senderToken,
            @Parameter(description = "Токен отправителя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String recipientToken
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(bankServiceV2.transferToAnotherBank(
                        recipientAccountId,
                        dto,
                        senderAccountId,
                        senderToken,
                        recipientToken
                ));
    }

    //перемещенние валюты с одного счет на другой посредством списания
    @PostMapping("/buy-currency")
    @Operation(summary = "Купить валюту с одного счёта на другой")
    @ApiResponse(responseCode = "202",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = BigDecimal.class))
    )
    public ResponseEntity<BigDecimal> buyCurrency(
            @RequestBody BuyCurrencyDtoRequest dtoRequest,
            @Parameter(description = "Токен пользователя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String userToken
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(bankServiceV2.buyCurrency(
                dtoRequest.baseAccountId(),
                dtoRequest.targetAccountId(),
                dtoRequest.amountMoney(),
                userToken,
                dtoRequest.currencyCode()));
    }
}

