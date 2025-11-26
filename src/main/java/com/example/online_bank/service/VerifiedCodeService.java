package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.VerifiedCode;
import com.example.online_bank.enums.VerifiedCodeType;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.repository.VerifiedCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifiedCodeService {
    private final VerifiedCodeRepository verifiedCodeRepository;

    public void save(VerifiedCode verifiedCode) {
        verifiedCodeRepository.save(verifiedCode);
    }

    public VerifiedCode createVerifiedCode(
            String verifiedCode,
            User user,
            LocalDateTime expirationDate,
            VerifiedCodeType type
    ) {
        return VerifiedCode.builder()
                .id(UUID.randomUUID())
                .expiresAt(expirationDate)
                .verifiedCode(verifiedCode)
                .createdAt(LocalDateTime.now())
                .user(user)
                .isVerified(FALSE)
                .codeType(type)
                .build();
    }

    public LocalDateTime createExpirationDate(int seconds) {
        return LocalDateTime.now().plusSeconds(seconds);
    }

    public void cleanVerifiedCodes(Long userId) {
        verifiedCodeRepository.deleteAllByIsVerifiedTrueAndUser_id(userId);
    }

    /**
     * Очистка всех старых кодов
     */
    public void clearOldCodes() {
        List<VerifiedCode> oldCodes = verifiedCodeRepository.findAllByExpiresAtBefore(LocalDateTime.now());
        verifiedCodeRepository.deleteAll(oldCodes);
    }

    /**
     * Возвращает true после того как код для верификации был найден
     * производит изменения в репозитории
     * если код не был найден - вернет false
     */
    public boolean validateCode(User user, String code, VerifiedCodeType type) {
        if (user.getIsVerified()) {
            log.debug("Пользователь уже верифицирован");
            throw new EntityAlreadyVerifiedException("Пользователь уже верифицирован");
        }

        LocalDateTime now = LocalDateTime.now();

        return verifiedCodeRepository
                .findByVerifiedCodeAndUser_IdAndCodeTypeAndIsVerifiedIsFalseAndExpiresAtAfter(code, user.getId(), type, now)
                .map(verifiedCode -> {
                    verifiedCode.setIsVerified(TRUE);
                    verifiedCodeRepository.save(verifiedCode);
                    return true;
                })
                .orElse(false);
    }
}