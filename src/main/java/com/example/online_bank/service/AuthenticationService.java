package com.example.online_bank.service;


import com.example.online_bank.domain.dto.AuthenticationRequest;
import com.example.online_bank.domain.dto.AuthenticationResponseDto;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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
        User user = userService
                .findByEmail(dtoRequest.email())
                .orElseThrow(EntityNotFoundException::new);

        boolean isVerified = userService.verifyEmailCode(user.getId(), dtoRequest.code());
        if (!isVerified) {
            throw new BadCredentialsException("Введенный код не действителен");
        }

        UserContainer userContainer = userMapper.toUserContainer(user);
        verifiedCodeService.cleanVerifiedCodes(user.getId());

        String accessToken = tokenService.getAccessToken(userContainer);
        String refreshToken = tokenService.getRefreshToken(userContainer);
        String idToken = tokenService.getIdToken(userContainer);

        return new AuthenticationResponseDto(Set.of(accessToken, refreshToken, idToken));
    }
}