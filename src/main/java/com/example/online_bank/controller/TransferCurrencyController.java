package com.example.online_bank.controller;

import com.example.online_bank.dto.integration.v1.ExchangeRateDtoRequest;
import com.example.online_bank.service.transfer.TransferCurrencyServiceV3;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Валютный Сервис", description = "Методы по работе с курсами валют")
@RestController
@RequestMapping("/currency-transfer")
@RequiredArgsConstructor
public class TransferCurrencyController {
    private final TransferCurrencyServiceV3 transferCurrencyServiceV3;

    @PostMapping("/create-exchange-rate")
    @Operation(summary = "создать обменный курс")
    @ApiResponse(responseCode = "202", content = @Content(mediaType = "text/plain"))
    public ResponseEntity<?> createExchangeRate(@RequestBody ExchangeRateDtoRequest dtoRequest) {
        transferCurrencyServiceV3.createExchangeRate(
                dtoRequest.baseCurrency(),
                dtoRequest.targetCurrency(),
                dtoRequest.targetRate()
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
