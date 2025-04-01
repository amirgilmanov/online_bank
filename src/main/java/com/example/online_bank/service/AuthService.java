package com.example.online_bank.service;


import com.example.online_bank.entity.AuthUsers;
import com.example.online_bank.entity.User;
import com.example.online_bank.exception.AuthException;
import com.example.online_bank.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.online_bank.util.NumberEncryptUtil.encryptPhoneNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authenticationRepository;
    private final TokenService tokenService;
    private static final String AUTH_ERROR_MESSAGE = "Неверный номер или пин-код";

    @Transactional
    public void save(String pinCode, User user) {
        AuthUsers entity = AuthUsers.builder()
                .pinCode(pinCode)
                .user(user).build();
        authenticationRepository.save(entity);
    }

    @Transactional
    public String signIn(String phoneNumber, String pinCode) {
        String encryptedPhoneNumber = encryptPhoneNumber(phoneNumber);
        log.info("Входящий номер телефона - {}", encryptedPhoneNumber);
        AuthUsers authUsers = authenticationRepository.findByUser_PhoneNumber(phoneNumber)
                .orElseThrow(() -> new AuthException(AUTH_ERROR_MESSAGE));
        validatePinCode(pinCode, authUsers.getPinCode());
        return tokenService.createAndSaveToken(authUsers.getUser());
    }

    private void validatePinCode(String pinCode, String actualPinCode) {
        log.info("Валидация пин-кода");
        log.debug("Входящий пин-код - {}", pinCode);
        log.debug("Реальный пин-код - {}", actualPinCode);
        if (!Objects.equals(pinCode, actualPinCode)) {
            throw new AuthException(AUTH_ERROR_MESSAGE);
        }
    }
}
