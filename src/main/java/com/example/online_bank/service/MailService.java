package com.example.online_bank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromTo;

    /**
     * @param to Адрес получателя (example@gmail.com)
     * @param subject Тема письма (Ваш код подтверждения)
     * @param text Содержимое письма (Здравствуйте, ваш код подтверждения)
     */
    public void sendMail(String to, String subject, String text) {
        log.info("Отправка сообщения: {}, от: {}", to, fromTo);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            simpleMailMessage.setFrom(fromTo);

            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
