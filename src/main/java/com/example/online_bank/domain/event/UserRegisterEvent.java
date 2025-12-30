package com.example.online_bank.domain.event;

/**
 * @param email email
 * @param code  otp
 */
public record UserRegisterEvent(String email, String code) {
}
