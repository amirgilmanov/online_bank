package com.example.online_bank.service.impl;

import com.example.online_bank.service.MailService;
import com.example.online_bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements NotificationService {
    private final MailService mailService;
    private final static String EMAIL_SUBJECT = "Код подтверждения";
    private final static String BODY_TEXT = "Ваш код регистрации подтверждения: ";

    /**
     * @param destination      Кому отправить письмо
     * @param verificationCode Код подтверждения
     */
    @Override
    public void sendOtpCode(String destination, String verificationCode) {

        mailService.sendMail(destination, EMAIL_SUBJECT, BODY_TEXT + verificationCode);
    }
}
