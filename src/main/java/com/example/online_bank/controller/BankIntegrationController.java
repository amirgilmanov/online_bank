package com.example.online_bank.controller;

import com.example.online_bank.dto.BuyCurrencyDto;
import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.TransactionDto;
import com.example.online_bank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
@Tag(name = "Интеграция банковV2", description = "Методы по рабе интеграции между банками")
public class BankIntegrationController {
    private final BankService bankService;

    @GetMapping("/get-bank-info")
    @Operation(summary = "Получить название текущего банка")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class, example = "EuroBank")
            )
    )
    public String info() {
        return bankService.info();
    }

    /**
     * Сделать перевод другому клиенту
     * @param dto содержит в себе номер счета получателя и отправителя, номера их банков
     * @return информацию изменения баланса получателя
     */
    @Operation(summary = "Сделать перевод в другой банк")
    @PostMapping("/transfer")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OperationDtoResponse.class))
    )
    public List<OperationDtoResponse> receiveTransferMoneyToPartnerBank(@RequestBody TransactionDto dto) {
        return bankService.transfer(dto);
    }

    @PostMapping("/buy-currency")
    @Operation(summary = "Купить валюту с одного счёта на другой")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = BigDecimal.class)
            )
    )
    public List<OperationDtoResponse> buyCurrency(
            @RequestBody BuyCurrencyDto dto,
            @Parameter(description = "Токен пользователя",
                    example = "online4c314d57-cbd0-4a83-9ce3-943e95b277a9token")
            @RequestHeader String token
    ) {
        return bankService.buyCurrency(dto, token);
    }
}

