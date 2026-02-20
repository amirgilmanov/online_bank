package com.example.online_bank.service;

import com.example.online_bank.domain.dto.RegenerateOtpDto;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.VerifiedCode;
import com.example.online_bank.domain.event.SendOtpEvent;
import com.example.online_bank.enums.VerifiedCodeType;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.repository.VerifiedCodeRepository;
import com.example.online_bank.util.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifiedCodeService {
    public static final String RESEND_CODE_MESSAGE = "Повторная отправка кода";
    private final VerifiedCodeRepository verifiedCodeRepository;
    private final EventPublisherService<SendOtpEvent> eventPublisherService;

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

    public void cleanAllCodes(Long userId) {
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
     * Ищет код. Если найден, меняет флаг на аутентифицированного
     */
    public void validateCode(User user, String code, VerifiedCodeType type, boolean isAuthenticated) throws VerificationOtpException {
        LocalDateTime now = LocalDateTime.now();
        if (!isAuthenticated) {
            VerifiedCode verifiedCode = verifiedCodeRepository
                    .findByVerifiedCodeAndUser_IdAndCodeTypeAndIsVerifiedIsFalseAndExpiresAtAfter(
                            code, user.getId(), type, now)
                    .orElseThrow(() ->
                            new VerificationOtpException("Ошибка верификации. Запросите новый код. Первая верификация"));
            log.info("Otp код был найден и будет верифицирован {}", verifiedCode);
            verifiedCode.setIsVerified(true);
            verifiedCodeRepository.save(verifiedCode);
        }
//fixme нужно переотправлять код
        if (isAuthenticated) {
            VerifiedCode verifiedCode = verifiedCodeRepository.findByVerifiedCodeAndUser_IdAndCodeTypeAndIsVerifiedIsTrueAndExpiresAtAfter(code, user.getId(), type, now).orElseThrow(() ->
                    new VerificationOtpException("Ошибка верификации. Запросите новый код"));
            log.info("Otp код был найден и будет верифицирован {}", verifiedCode);
        }

    }

    @Transactional
    public void regenerateOtp(RegenerateOtpDto dto) {
        log.info("Обновление отп");
        String newOtp = CodeGeneratorUtil.generateOtp();
        LocalDateTime newExpDate = createExpirationDate(200);
        verifiedCodeRepository.updateVerifiedCodeByUser_Email(dto.email(), newOtp, newExpDate);
        log.info("Публикация отп");
        eventPublisherService.publishEvent(new SendOtpEvent(dto.email(), newOtp, RESEND_CODE_MESSAGE));
    }
}