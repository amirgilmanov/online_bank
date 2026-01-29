package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.BankPartnerDto;
import com.example.online_bank.service.BankPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-partner")
@RequiredArgsConstructor
public class PartnerBankController {
    private final BankPartnerService bankPartnerService;

    @PostMapping
    public void create(@RequestBody BankPartnerDto dto){
        bankPartnerService.create(dto.name(), dto.category());
    }
}
