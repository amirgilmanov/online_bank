    package com.example.online_bank.service;


    import com.example.online_bank.domain.dto.AuthenticationResponseDto;
    import com.example.online_bank.domain.dto.AuthenticationRequest;
    import com.example.online_bank.security.token.EmailAuthenticationToken;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class AuthenticationService {
        private final TokenService tokenService;
        private final AuthenticationManager authenticationManager;

        @Transactional
        public AuthenticationResponseDto signIn(AuthenticationRequest dtoRequest) {
            EmailAuthenticationToken authToken = new EmailAuthenticationToken(dtoRequest.email(), dtoRequest.code());
            Authentication authResult = authenticationManager.authenticate(authToken);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
            String accessToken = tokenService.getAccessToken(authResult);
            String refreshToken = tokenService.getRefreshToken(authResult);
            String idToken = tokenService.getIdToken(authResult);
            return new AuthenticationResponseDto(accessToken, refreshToken, idToken);
        }
    }
