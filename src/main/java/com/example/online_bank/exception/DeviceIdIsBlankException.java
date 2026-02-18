package com.example.online_bank.exception;

import lombok.Getter;

@Getter
public class DeviceIdIsBlankException extends RuntimeException {
    private final String deviceId;
    private final String message;

    public DeviceIdIsBlankException(String deviceId, String message) {
        super(message);
        this.deviceId = deviceId;
        this.message = message;
    }
}
