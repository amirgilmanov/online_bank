package com.example.online_bank.service;


import com.example.online_bank.domain.dto.AuthenticationRequest;
import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.RefreshToken;
import com.example.online_bank.domain.entity.TokenFamily;
import com.example.online_bank.domain.entity.TrustedDevice;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.TokenStatus;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokenService tokenService;
    private final UserService userService;
    private final VerifiedCodeService verifiedCodeService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponseDto signIn(AuthenticationRequest dtoRequest) {
        try {
            // 1. Находим пользователя по email
            User user = userService.findByEmail(dtoRequest.email())
                    .orElseThrow(EntityNotFoundException::new);

            //TODO если user.getIsVerified() == true, ты проверяешь: есть ли этот deviceId в таблице trusted_devices этого пользователя. <- это первичная логика
            //2. Смотрим, чтобы не был верифицирован
            if (user.getIsVerified()) {
                log.warn("Пользователь уже верифицирован");
                throw new EntityAlreadyVerifiedException("Пользователь уже верифицирован");
            }
            //2 сверяем otp code
            userService.verifyEmailCode(user, dtoRequest.code());

            //3. конвертируем в userContainer
            UserContainer userContainer = userMapper.toUserContainer(user);
            log.info("Очистка старых кодов");
            verifiedCodeService.cleanVerifiedCodes(user.getId());

            //4. Создаем токены
            log.info("Создание токенов");
            String accessToken = tokenService.getAccessToken(userContainer);
            //String refreshToken = tokenService.getRefreshToken(userContainer);
            Map<String, Object> refreshTokenWithDate = tokenService.getRefreshTokenWithDate(userContainer);
            String token = (String) refreshTokenWithDate.get("token");

            LocalDateTime expiredAt = LocalDateTime.ofInstant(
                    ((Date) refreshTokenWithDate.get("expiredAt")).toInstant(),
                    ZoneId.systemDefault()
            );
            LocalDateTime createdAt = LocalDateTime.ofInstant(
                    ((Date) refreshTokenWithDate.get("createdAt")).toInstant(),
                    ZoneId.systemDefault()
            );
            String idToken = tokenService.getIdToken(userContainer);

            //5 создаем trusted_device
            String deviceId = UUID.randomUUID().toString();
            String deviceName = dtoRequest.deviceName();
            TrustedDevice trustedDevice = TrustedDevice.builder()
                    .deviceName(deviceName)
                    .deviceId(deviceId)
                    .userAgent(dtoRequest.userAgent())
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .build();

            TokenFamily tokenFamily = TokenFamily.builder()
                    .isBlocked(false)
                    .trustedDevice(trustedDevice)
                    .user(user)
                    .build();

            RefreshToken.builder()
                    .tokenHash(bCryptPasswordEncoder.encode(token))
                    .expiresAt(expiredAt)
                    .createdAt(createdAt)
                    .revokedAt(null)
                    .status(TokenStatus.CREATED)
                    .family(tokenFamily)
                    .build();

            return new AuthenticationResponseDto(
                    Map.of(
                            "accessToken", accessToken,
                            "refreshToken", token,
                            "idToken", idToken
                    )
            );
        } catch (VerificationOtpException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверные учетные данные");
        }
    }
}