package com.example.online_bank.service;

import com.example.online_bank.domain.entity.RefreshToken;
import com.example.online_bank.enums.TokenStatus;
import com.example.online_bank.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }

    public void revoke(RefreshToken refreshToken){
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshToken.setStatus(TokenStatus.REVOKED);
        refreshTokenRepository.save(refreshToken);
    }


}
