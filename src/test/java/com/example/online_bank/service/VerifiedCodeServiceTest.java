package com.example.online_bank.service;

import com.example.online_bank.repository.VerifiedCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
class VerifiedCodeServiceTest {
    @Mock
    private VerifiedCodeRepository verifiedCodeRepository;
    @InjectMocks
    private VerifiedCodeService verifiedCodeService;

    @Test
    @DisplayName("Успешное создание даты истечения")
    void successCreateExpirationDate() {
        //arr
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = verifiedCodeService.createExpirationDate(20);
        //act
        Assertions.assertEquals(now.plusSeconds(20), expirationDate);
    }
}