package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.*;
import com.example.online_bank.enums.TokenStatus;
import com.example.online_bank.exception.DeviceNotFoundException;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.service.*;
import com.example.online_bank.util.CodeGeneratorUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final TrustedDeviceService trustedDeviceService;
    private final VerifiedCodeService verifiedCodeService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final TokenFamilyService tokenFamilyService;

    //для того чтобы показать что рефреш токен истек и потребуется вход с паролем. т.к. устройство есть в списке доверенных,
    //а если бы его там не было то при попытке входа с паролем и почтой пришлось бы сделать подтверждение владения почтой т.е.
    //входим - получаем данные с устройства
    //устройство есть а списке доверенных для пользователя с конкретным емайл?
    //если есть, то окей входим
    //если нет, то отправляем ему код на почту т.к. произошел вход с нового устройства

  //если входим со старого устройства и пароль с почтой верный, то добавляем устройство в семью токенов
    @PostMapping
    public void login(String email, String password, String deviceId, String deviceName, String userAgent) {
        User user = userService.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        //если доверенного устройства не нашлось, то тогда отправляем otp code на почту
        if (trustedDeviceService.existsByDeviceIdAndUser_email(deviceId, email)) {
            String code = CodeGeneratorUtil.generateOtp();
            LocalDateTime expireDate = verifiedCodeService.createExpirationDate(200);
            VerifiedCode verifiedCode = verifiedCodeService.createVerifiedCode(code, user, expireDate, EMAIL);

            verifiedCodeService.save(verifiedCode);
            notificationService.sendOtpCode(email, code);
            //верно ли завершать этот блок с исключением? и в адвайсе прокинуть http код 401 с сообщением проверить почтовый ящик для подтверждения
            throw new DeviceNotFoundException("Подтвердите вход с помощью проверочного кода");
        }

        //если пароль не совпал, то выкидываем ошибку
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Логин или пароль не совпадает");
        }

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

        //если пароль правильный, то создать новую семью с переданным устройством и рефреш токен
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
        TrustedDevice trustedDevice = TrustedDevice.builder()
                .deviceName(deviceName)
                .deviceId(deviceId)
                .userAgent(userAgent)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        trustedDeviceService.save(trustedDevice);

        TokenFamily tokenFamily = TokenFamily.builder()
                .isBlocked(false)
                .trustedDevice(trustedDevice)
                .user(user)
                .build();
        tokenFamilyService.save(tokenFamily);

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(bCryptPasswordEncoder.encode(token))
                .expiresAt(expiredAt)
                .createdAt(createdAt)
                .revokedAt(null)
                .status(TokenStatus.CREATED)
                .family(tokenFamily)
                .build();
        refreshTokenService.save(refreshToken);

         new AuthenticationResponseDto(
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", token,
                        "idToken", idToken
                )
        );
    }
}
