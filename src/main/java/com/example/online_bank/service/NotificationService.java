package com.example.online_bank.service;


public interface NotificationService {

    void sendOtpCode(String destination, String verificationCode, String bodyText);
}