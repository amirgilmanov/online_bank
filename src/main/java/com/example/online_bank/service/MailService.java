package com.example.online_bank.service;

import com.example.online_bank.exception.SendEmailException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Setter
@Getter
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromTo;

    /**
     * @param to      Адрес получателя (example@gmail.com)
     * @param subject Тема письма (Ваш код подтверждения)
     * @param text    Содержимое письма (Здравствуйте, ваш код подтверждения)
     */
    public void sendMail(String to, String subject, String text) {

        if (fromTo == null) {
            throw new NullPointerException("fromTo is null");
        }

        log.info("From: {}", fromTo);
        log.info("Отправка сообщения пользователю: {}", to);
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            simpleMailMessage.setFrom(fromTo);

            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new SendEmailException(e);
        }
    }
}