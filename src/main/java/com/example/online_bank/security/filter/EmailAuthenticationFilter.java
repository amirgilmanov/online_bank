package com.example.online_bank.security.filter;

import com.example.online_bank.security.token.EmailAuthenticationToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.BufferedReader;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final String AUTH_PATH_PREFIX = "/api/authentication";

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestBodyStr = readBody(request);

        log.debug("Парсим и читаем json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestBodyStr);
        String email = jsonNode.get("email").asText();
        String code = jsonNode.get("code").asText();
        EmailAuthenticationToken authToken = new EmailAuthenticationToken(email, code);

        Authentication authResult = this.authenticationManager.authenticate(authToken);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * True = "Да, НЕ фильтруй" → пропускаем
     * false = "Нет, фильтруй" → выполняем фильтр
     *
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        return !requestPath.startsWith(AUTH_PATH_PREFIX);
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        log.debug("Читаем тело запроса");
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String requestBodyStr = sb.toString();
        log.info("Тело запроса - {}", requestBodyStr);
        return requestBodyStr;
    }
}
