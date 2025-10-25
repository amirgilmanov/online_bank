package com.example.online_bank.security.filter;

import com.example.online_bank.security.token.JwtRequestToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor

/*
 * Этот фильтр включится, когда клиент обратиться в защищенную область
 * Если область не защищена, то тогда этот фильтр не будет действовать
 */
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Метод, чтобы выстроиться в цепочку фильтров Spring
     * Если запрос в защищенную область если в нем есть токен(JWT), этот фильтр
     * будет перекладывать данные из токена в Security контекст
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String jwtToken;

        String pathInfo = request.getRequestURI();
        if (isPublicUri(pathInfo)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            Authentication authResult = this.authenticationManager.authenticate(new JwtRequestToken(jwtToken));

            // Проверка на то, что контекст не занят кем-то другим
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean isPublicUri(String uri) {
        return uri.startsWith("/api/sign-up") ||
                uri.startsWith("/swagger-ui/index.html") ||
                uri.startsWith("/test") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-resources/") ||
                uri.startsWith("/webjars/") ||
                uri.startsWith("/configuration") ||
                uri.endsWith(".css") ||
                uri.endsWith(".js") ||
                uri.endsWith(".png") ||
                uri.endsWith(".ico") ||
                uri.startsWith("/test/") ||
                uri.startsWith("/") ||
                uri.startsWith("/api/authentication/email");
    }
}
