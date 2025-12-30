package com.example.online_bank.exception;

import lombok.experimental.StandardException;
import org.springframework.mail.MailException;

@StandardException
public class SendEmailException extends RuntimeException {
    public SendEmailException(MailException e) {
    }
}
