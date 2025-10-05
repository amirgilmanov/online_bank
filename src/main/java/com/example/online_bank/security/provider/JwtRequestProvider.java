package com.example.online_bank.security.provider;

import com.example.online_bank.domain.model.JwtUserDetails;
import com.example.online_bank.security.jwt.service.impl.JwtServiceImpl;
import com.example.online_bank.security.token.JwtRequestToken;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Получаем информацию о пользователе.
        // Проверяем пользователя.
        //Библиотека jjwt проверяет подпись, дату истечения
        JwtRequestToken jwtRequestToken = (JwtRequestToken) authentication;
        String token = jwtRequestToken.getToken();
        try {
            Claims jwtClaims = jwtService.getPayload(token);

            // TODO: проверка blacklist
            // if (tokenBlacklistService.isBlacklisted(token)) {
            //     throw new BadCredentialsException("Token is blacklisted");
            // }

            Collection<? extends GrantedAuthority> authorities = jwtService.getAuthoritiesForAuthToken(jwtClaims);
            String uuid = jwtService.getUuid(jwtClaims);
            String username = jwtService.getUsername(jwtClaims);

            JwtUserDetails details = new JwtUserDetails(uuid, username, authorities);
            return new JwtRequestToken(authorities, details);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Неверный или просроченный токен");
        }
    }

    /**
     * @param authentication поддерживаемый тип authentication
     * @return поддерживает ли типа или нет
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtRequestProvider.class.isAssignableFrom(authentication);
    }
}
