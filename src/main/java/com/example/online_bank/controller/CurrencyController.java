package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.*;
import com.example.online_bank.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Валютный Сервис", description = "Методы по работе с курсами валют")
@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "Создать обменный курс")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "text/plain"))
    public ResponseEntity<RateResponseDto> createExchangeRate(@RequestBody CreateExchangeRateDto dtoRequest) {
        currencyService.create(
                dtoRequest.baseCurrency(),
                dtoRequest.targetCurrency(),
                dtoRequest.rate()
        );
        return ResponseEntity.status(CREATED).body(currencyService.create(
                dtoRequest.baseCurrency(),
                dtoRequest.targetCurrency(),
                dtoRequest.rate()
        ));
    }

    @GetMapping("/convert")
    @Operation(summary = "Конвертировать валюту")
    public ConvertCurrencyResponse convertCurrency(@RequestBody ConvertCurrencyDto dtoRequest) {
        return currencyService.convertCurrency(dtoRequest.baseCurrency(), dtoRequest.targetCurrency(), dtoRequest.amount());
    }

    @GetMapping("/find-rate")
    @Operation(summary = "Найти курс")
    public ConvertCurrencyResponse findRate(@RequestBody RateRequestDto dto) {
        return currencyService.findRate(dto.from(), dto.to());
    }
}
