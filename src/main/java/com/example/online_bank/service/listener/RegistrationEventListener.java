package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.UserRegisterEvent;
import com.example.online_bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener {
    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handle(UserRegisterEvent event) {
        notificationService.sendOtpCode(event.email(), event.code());
    }
}
