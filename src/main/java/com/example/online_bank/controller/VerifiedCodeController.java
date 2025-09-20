package com.example.online_bank.controller;

import com.example.online_bank.service.VerifiedCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class VerifiedCodeController {
    private final VerifiedCodeService verifiedCodeService;

    @DeleteMapping
    public void deleteOldCode() {
        verifiedCodeService.clearOldCodes();
    }
}
