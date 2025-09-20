package com.example.online_bank.service;


public interface NotificationService {

    void sendVerificationCode(String destination, String verificationCode);
}
