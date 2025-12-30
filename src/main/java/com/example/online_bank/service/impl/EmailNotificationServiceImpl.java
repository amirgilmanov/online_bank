package com.example.online_bank.service.impl;

import com.example.online_bank.service.MailService;
import com.example.online_bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl implements NotificationService {
    private final MailService mailService;
    private final static String EMAIL_SUBJECT = "Код подтверждения";
    private final static String BODY_TEXT = "Ваш код регистрации подтверждения: ";

    /**
     * @param destination      Кому отправить письмо
     * @param verificationCode Код подтверждения
     */
    @Override
    @Async
    public void sendOtpCode(String destination, String verificationCode) {
        log.info("Отправка otp кода");
        mailService.sendMail(destination, EMAIL_SUBJECT, BODY_TEXT + verificationCode);
    }
}
