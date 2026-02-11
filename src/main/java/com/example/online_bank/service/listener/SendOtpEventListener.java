package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.SendOtpEvent;
import com.example.online_bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
public class SendOtpEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    @Async
    public void onSendOtpEventListener(SendOtpEvent event) {
        notificationService.sendOtpCode(event.email(), event.code());
    }
}
