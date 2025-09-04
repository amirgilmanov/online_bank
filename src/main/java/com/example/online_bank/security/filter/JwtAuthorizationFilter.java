package com.example.online_bank.security.filter;

import com.example.online_bank.security.jwt.service.impl.JwtUtilImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authzHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        String pathInfo = request.getRequestURI();
        if (pathInfo.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authzHeader != null && authzHeader.startsWith("Bearer ")) {
            jwtToken = authzHeader.substring(7);
            Date date = new Date();

            // Получаем информацию о пользователе.
            // Проверяем пользователя.
            //Библиотека jjwt проверяет подпись, дату истечения
            try {
                username = jwtUtil.getUserName(jwtToken);
            } catch (SignatureException e) {
                log.warn("Время жизни токена вышло");
            } catch (ExpiredJwtException e) {
                log.warn("Токен устарел");
            }
        }

        //Проверка на то, что контекст не занят кем-то другим
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //TODO узнать что здесь происходит
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtUtil.getUserAuthoritiesFromToken(jwtToken).stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
