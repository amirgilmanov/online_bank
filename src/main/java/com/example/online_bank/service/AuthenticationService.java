package com.example.online_bank.service;


import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.dto.VerificationRequest;
import com.example.online_bank.domain.entity.*;
import com.example.online_bank.domain.event.SendOtpEvent;
import com.example.online_bank.exception.DeviceNotFoundException;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.mapper.UserMapper;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.example.online_bank.enums.TokenStatus.CREATED;
import static com.example.online_bank.enums.TokenStatus.REVOKED;
import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;
import static com.example.online_bank.util.CodeGeneratorUtil.generateOtp;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public static final String SECURITY_MESSAGE = "Обнаружена попытка взлома. Подтвердите вход через электронное письмо. Если это были не вы проигнорируйте это письмо";
    private final TokenService tokenService;
    private final UserService userService;
    private final VerifiedCodeService verifiedCodeService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TrustedDeviceService trustedDeviceService;
    private final RefreshTokenService refreshTokenService;
    private final TokenFamilyService tokenFamilyService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final String CREATED_AT = "created_at";
    private static final String EXPIRED_AT = "expired_at";

    @Transactional
    public AuthenticationResponseDto firstLogIn(VerificationRequest dtoRequest) {
        //5 создаем trusted_device
        String deviceId = UUID.randomUUID().toString();
        return checkVerifyCode(dtoRequest, deviceId);
    }

    private AuthenticationResponseDto checkVerifyCode(VerificationRequest dto, String deviceId) {
        try {
            // 1. Находим пользователя по email
            User user = userService.findByEmail(dto.email())
                    .orElseThrow(EntityNotFoundException::new);

            //2. Смотрим, чтобы не был верифицирован
            if (user.getIsVerified()) {
                log.warn("Пользователь уже верифицирован");
                throw new EntityAlreadyVerifiedException("Пользователь уже верифицирован");
            }
            //2 сверяем otp code
            userService.verifyEmailCode(user, dto.code());

            //3. конвертируем в userContainer
            UserContainer userContainer = userMapper.toUserContainer(user);
            log.info("Очистка старых кодов");
            verifiedCodeService.cleanVerifiedCodes(user.getId());

            //создаем access и id
            AuthenticationResponseDto tokens = createAccessAndIdTokens(userContainer);

            //создаем refresh
            Map<String, Object> refreshAndDateMap = tokenService.getRefreshTokenWithDate(userContainer);
            String refreshToken = (String) refreshAndDateMap.get("token");
            LocalDateTime expiredAt = getTime(EXPIRED_AT, refreshAndDateMap);
            LocalDateTime createdAt = getTime(CREATED_AT, refreshAndDateMap);

            TokenFamily tokenFamily = createFamilyAndTrustedDevice(dto.deviceName(), deviceId, user, dto.userAgent());
            createRefreshTokenEntity(refreshToken, tokenFamily, expiredAt, createdAt);
            putRefreshTokenToResponse(tokens, refreshToken);
            return tokens;
        } catch (VerificationOtpException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверные учетные данные");
        }
    }

    //🔹 Тихий вход (refresh rotation)
    //access expired
    //→ refresh
    //
    //1. refresh найден?
    //   нет → 401
    //
    //2. refresh.status == REVOKED ?
    //   → reuse detected
    //   → block TokenFamily
    //   → revoke ALL refresh in family
    //   → REQUIRE OTP
    //
    //3. family.isBlocked == true ?
    //   → REQUIRE OTP
    //
    //4. refresh.expiresAt < now ?
    //   → 401 (expired)
    //
    //5. OK:
    //   → revoke old refresh
    //   → create new refresh
    //   → return access + refresh
    @Transactional
    public AuthenticationResponseDto silentLogin(String refreshToken, String deviceName, String userAgent, String deviceId) {
        String encodedToken = bCryptPasswordEncoder.encode(refreshToken);
        RefreshToken refreshTokenByTokenHash = refreshTokenService.findByTokenHash(encodedToken);
        validateToken(refreshToken);
        TokenFamily family = refreshTokenByTokenHash.getFamily();
        User user = family.getUser();

        checkReuseDetection(refreshTokenByTokenHash, family);
        log.info("start revoke old  token");
        refreshTokenService.revoke(refreshTokenByTokenHash);
        UserContainer userContainer = userMapper.toUserContainer(user);

        //4. Создаем токены
        //создаем access и id
        AuthenticationResponseDto tokens = createAccessAndIdTokens(userContainer);

        //создаем refresh
        Map<String, Object> refreshAndDateMap = tokenService.getRefreshTokenWithDate(userContainer);
        String newRefreshToken = (String) refreshAndDateMap.get("token");
        LocalDateTime expiredAt = getTime(EXPIRED_AT, refreshAndDateMap);
        LocalDateTime createdAt = getTime(CREATED_AT, refreshAndDateMap);
        RefreshToken newRefreshTokenEntity = createRefreshTokenEntity(newRefreshToken, family, expiredAt, createdAt);
        refreshTokenService.save(newRefreshTokenEntity);

        putRefreshTokenToResponse(tokens, refreshToken);
        return tokens;
        createTokenHelper(user, deviceName, userAgent, deviceId, family);
    }

    private AuthenticationResponseDto createTokenHelper(User user, String deviceName, String userAgent, String deviceId, TokenFamily tokenFamily) {
        //Если пароль правильный, то создать новую семью с переданным устройством и refresh токен
        //3. конвертируем в userContainer
        UserContainer userContainer = userMapper.toUserContainer(user);

        //создаем access и id
        AuthenticationResponseDto tokens = createAccessAndIdTokens(userContainer);

        //создаем refresh
        Map<String, Object> refreshAndDateMap = tokenService.getRefreshTokenWithDate(userContainer);
        String refreshToken = (String) refreshAndDateMap.get("token");
        LocalDateTime expiredAt = getTime(EXPIRED_AT, refreshAndDateMap);
        LocalDateTime createdAt = getTime(CREATED_AT, refreshAndDateMap);

       // TokenFamily tokenFamily = createFamilyAndTrustedDevice(deviceName, deviceId, user, userAgent);
        createRefreshTokenEntity(refreshToken, tokenFamily, expiredAt, createdAt);
        putRefreshTokenToResponse(tokens, refreshToken);
        return tokens;

    }

    @Transactional
    public void logout(String refreshToken) {
        String encodedToken = bCryptPasswordEncoder.encode(refreshToken);
        RefreshToken refreshTokenByTokenHash = refreshTokenService.findByTokenHash(encodedToken);
        validateToken(refreshToken);
        TokenFamily family = refreshTokenByTokenHash.getFamily();

        checkReuseDetection(refreshTokenByTokenHash, family);
        revokeTokenAndBlockFamily(family, refreshTokenByTokenHash);
    }

    private void validateToken(String refreshToken) {
        try {
            jwtService.getPayload(refreshToken);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверный или просроченный токен");
        }
    }

    private void revokeTokenAndBlockFamily(TokenFamily tokenFamily, RefreshToken refreshToken) {
        log.info("start revoke old  token and family");
        tokenFamilyService.blockFamily(tokenFamily);
        refreshTokenService.revoke(refreshToken);
    }

    private void checkReuseDetection(RefreshToken refreshTokenByTokenHash, TokenFamily family) {
        if (refreshTokenByTokenHash.getStatus().equals(REVOKED) && family.getIsBlocked()) {
            log.error("Reuse detected");
            tokenFamilyService.blockFamily(family);
            refreshTokenService.revokeAllByFamily(family);

            throw new SecurityException(SECURITY_MESSAGE);
        }
    }

    //если входим со старого/нового устройства и пароль с почтой верный, то добавляем устройство в семью токенов
    //если доверенного устройства не нашлось, то тогда отправляем otp code на почту
    //Вход с нового устройства
    //email
    //→ deviceId не найден
    //→ OTP
    //→ подтверждение OTP
    //→ create TrustedDevice
    //→ create TokenFamily
    //→ create RefreshToken
    //→ return access + refresh + deviceId
    public AuthenticationResponseDto login(String email, String password, String deviceId, String deviceName, String userAgent) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        //если доверенного устройства не нашлось, то тогда отправляем otp code на почту
        if (trustedDeviceService.existsByDeviceIdAndUser_email(deviceId, email)) {
            String code = generateOtp();
            LocalDateTime expireDate = verifiedCodeService.createExpirationDate(200);
            VerifiedCode verifiedCode = verifiedCodeService.createVerifiedCode(code, user, expireDate, EMAIL);
            verifiedCodeService.save(verifiedCode);
            SendOtpEvent event = new SendOtpEvent(email, code);
            applicationEventPublisher.publishEvent(event);
            //верно ли завершать этот блок с исключением? и в адвайсе прокинуть http код 401 с сообщением проверить почтовый ящик для подтверждения
            throw new DeviceNotFoundException("Подтвердите вход с помощью проверочного кода");
        } else {
            //если пароль не совпал, то выкидываем ошибку
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Логин или пароль не совпадает");
            }
        }
        return createTokenHelper(user, deviceId, deviceName, userAgent);
    }



    private void putRefreshTokenToResponse(AuthenticationResponseDto tokens, String refreshToken) {
        tokens.tokens().put("refreshToken", refreshToken);
    }

    private AuthenticationResponseDto createAccessAndIdTokens(UserContainer userContainer) {
        log.info("Создание токенов");
        String accessToken = tokenService.getAccessToken(userContainer);
        String idToken = tokenService.getIdToken(userContainer);

        return new AuthenticationResponseDto(Map.of("accessToken", accessToken, "idToken", idToken));
    }

    private TokenFamily createFamilyAndTrustedDevice(
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
        tokenFamilyService.save(tokenFamily);
        return tokenFamily;
    }

    public RefreshToken createRefreshTokenEntity(
            String token,
            TokenFamily tokenFamily,
            LocalDateTime expiredAt,
            LocalDateTime createdAt
    ) {
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(bCryptPasswordEncoder.encode(token))
                .expiresAt(expiredAt)
                .createdAt(createdAt)
                .revokedAt(null)
                .status(CREATED)
                .family(tokenFamily)
                .build();
        refreshTokenService.save(refreshToken);
        return refreshToken;
    }

    private LocalDateTime getTime(String timeType, Map<String, Object> map) {
        return LocalDateTime.ofInstant(
                ((Date) map.get(timeType)).toInstant(),
                ZoneId.systemDefault()
        );
    }
}