package com.example.online_bank.service;


import com.example.online_bank.domain.dto.AuthenticationRequest;
import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.exception.EntityAlreadyVerifiedException;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokenService tokenService;
    private final UserService userService;
    private final VerifiedCodeService verifiedCodeService;
    private final UserMapper userMapper;

    @Transactional
    public AuthenticationResponseDto signIn(AuthenticationRequest dtoRequest) {
        try {
            // 1. Находим пользователя по email
            User user = userService.findByEmail(dtoRequest.email())
                    .orElseThrow(EntityNotFoundException::new);

            //2. Смотрим, чтобы не был верифицирован
            if (user.getIsVerified()) {
                log.warn("Пользователь уже верифицирован");
                throw new EntityAlreadyVerifiedException("Пользователь уже верифицирован");
            }
            userService.verifyEmailCode(user, dtoRequest.code());

            //4. конвертируем в userContainer
            UserContainer userContainer = userMapper.toUserContainer(user);
            log.info("Очистка старых кодов");
            verifiedCodeService.cleanVerifiedCodes(user.getId());

            //5. Создаем токены
            log.info("Создание токенов");
            String accessToken = tokenService.getAccessToken(userContainer);
            String refreshToken = tokenService.getRefreshToken(userContainer);
            String idToken = tokenService.getIdToken(userContainer);


            return new AuthenticationResponseDto(Map.of("accessToken", accessToken, "refreshToken", refreshToken, "idToken", idToken));
        } catch (VerificationOtpException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }
}