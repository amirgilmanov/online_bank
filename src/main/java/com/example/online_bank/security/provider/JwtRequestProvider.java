package com.example.online_bank.security.provider;

import com.example.online_bank.domain.model.JwtUserDetails;
import com.example.online_bank.security.token.JwtRequestToken;
import com.example.online_bank.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestProvider implements AuthenticationProvider {
    private final JwtServiceImpl jwtService;

    /**
     * Получаем информацию о пользователе.
     * Проверяем пользователя.
     * Библиотека jjwt проверяет подпись, дату истечения
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Начало работы фильтра jwt аутентификации");

        JwtRequestToken jwtRequestToken = (JwtRequestToken) authentication;
        String token = jwtRequestToken.getToken();
        try {
            Claims jwtClaims = jwtService.getPayload(token);

            Collection<? extends GrantedAuthority> authorities = jwtService.mapRolesForSpringToken(jwtClaims);
            String uuid = jwtService.getSubject(jwtClaims);
            String username = jwtService.getUsername(jwtClaims);

            log.trace("Создаем JwtUserDetails");
            JwtUserDetails details = new JwtUserDetails(uuid, username, authorities);
            log.trace("Возвращаем аутентифицированный токен {}", details);

            return new JwtRequestToken(authorities, details);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверный или просроченный токен");
        }
    }

    /**
     * @param authentication поддерживаемый тип authentication
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtRequestToken.class.isAssignableFrom(authentication);
    }
}