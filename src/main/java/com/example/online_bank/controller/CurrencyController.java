package com.example.online_bank.controller;

import com.example.online_bank.dto.CreateExchangeRateDto;
import com.example.online_bank.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Валютный Сервис", description = "Методы по работе с курсами валют")
@RestController
@RequestMapping("/api/currency-transfer")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping("/create-exchange-rate")
    @Operation(summary = "создать обменный курс")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "text/plain"))
    public ResponseEntity<?> createExchangeRate(@RequestBody CreateExchangeRateDto dtoRequest) {
        currencyService.create(
                dtoRequest.baseCurrency(),
                dtoRequest.targetCurrency(),
                dtoRequest.rate()
        );
        return ResponseEntity.status(CREATED).build();
    }
}
