package com.example.online_bank.service;

import com.example.online_bank.domain.entity.RefreshToken;
import com.example.online_bank.domain.entity.TokenFamily;
import com.example.online_bank.enums.TokenStatus;
import com.example.online_bank.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void revoke(RefreshToken refreshToken) {
        refreshToken.setStatus(TokenStatus.REVOKED);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken findByTokenHash(String encodedToken) {
        return refreshTokenRepository.findRefreshTokenByTokenHash(encodedToken).orElseThrow(()-> new EntityNotFoundException("RefreshToken not found"));
    }

    public void revokeAllByFamily(TokenFamily family) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByFamily(family);
        tokens.forEach(token ->{
            token.setRevokedAt(LocalDateTime.now());
            token.setStatus(TokenStatus.REVOKED);
            refreshTokenRepository.save(token);
        });
    }
}
