package com.example.online_bank.service;

import com.example.online_bank.domain.entity.RefreshToken;
import com.example.online_bank.domain.entity.TokenFamily;
import com.example.online_bank.domain.entity.TrustedDevice;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.repository.TokenFamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenFamilyService {
    private final TokenFamilyRepository tokenFamilyRepository;
    private final RefreshTokenService refreshTokenService;
    private final TrustedDeviceService trustedDeviceService;

    public void save(TokenFamily tokenFamily) {
        tokenFamilyRepository.save(tokenFamily);
    }

    public void blockFamily(TokenFamily family) {
        family.setIsBlocked(true);
        tokenFamilyRepository.save(family);
    }

    public void revokeTokenAndBlockFamily(TokenFamily tokenFamily, RefreshToken refreshToken) {
        log.info("start revoke old  token and family");
        blockFamily(tokenFamily);
        refreshTokenService.revoke(refreshToken);
    }

    public TokenFamily createFamilyAndTrustedDevice(
            String deviceName,
            String deviceId,
            User user,
            String userAgent) {
        //5 создаем trusted_device
        TrustedDevice trustedDevice = TrustedDevice.builder()
                .deviceName(deviceName)
                .deviceId(deviceId)
                .userAgent(userAgent)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        trustedDeviceService.save(trustedDevice);

        //6 создаем family
        TokenFamily tokenFamily = TokenFamily.builder()
                .isBlocked(false)
                .trustedDevice(trustedDevice)
                .user(user)
                .build();
        save(tokenFamily);
        return tokenFamily;
    }


}
