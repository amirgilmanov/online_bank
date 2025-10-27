package com.example.online_bank.domain.dto;

/**
 * @param email email
 * @param code otp
 */
public record RegistrationDtoResponse(String email, String code) {
}
