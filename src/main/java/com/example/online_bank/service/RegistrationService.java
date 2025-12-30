package com.example.online_bank.service;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.dto.RegistrationDtoResponse;
import com.example.online_bank.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserMapper userMapper;
    private final RegistrationProcessor registrationProcessor;

    @Transactional
    public RegistrationDtoResponse signUp(RegistrationDto registrationDto) {
        return registrationProcessor.register(registrationDto, userMapper::toUser);
    }

    @Transactional
    public RegistrationDtoResponse adminSignUp(RegistrationDto registrationDto) {
        return registrationProcessor.register(registrationDto, userMapper::toUserAdmin);
    }
}