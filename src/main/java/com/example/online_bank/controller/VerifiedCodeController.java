package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.RegenerateOtpDto;
import com.example.online_bank.service.VerifiedCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class VerifiedCodeController {
    private final VerifiedCodeService verifiedCodeService;

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOldCode() {
        verifiedCodeService.clearOldCodes();
    }

    @PatchMapping("/update/otp")
    public ResponseEntity<Void> regenerateCode(@RequestBody RegenerateOtpDto dto) {
        verifiedCodeService.regenerateOtp(dto);
        return ResponseEntity.ok().build();
    }
}
