package com.example.online_bank.service;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.event.UserRegisterEvent;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.service.processor.RegistrationProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserMapper userMapper;
    private final RegistrationProcessor registrationProcessor;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void signUp(RegistrationDto registrationDto) {
        UserRegisterEvent event = registrationProcessor.register(registrationDto, userMapper::toUser);
        applicationEventPublisher.publishEvent(event);
    }

    @Transactional
    public void adminSignUp(RegistrationDto registrationDto) {
        UserRegisterEvent event = registrationProcessor.register(registrationDto, userMapper::toUserAdmin);
        applicationEventPublisher.publishEvent(event);
    }
}