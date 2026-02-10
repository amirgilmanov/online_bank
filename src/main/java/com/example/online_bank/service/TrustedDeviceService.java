package com.example.online_bank.service;

import com.example.online_bank.domain.entity.TrustedDevice;
import com.example.online_bank.repository.TrustedDeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrustedDeviceService {
    private final TrustedDeviceRepository trustedDeviceRepository;

    public void save(TrustedDevice trustedDevice) {
        trustedDeviceRepository.save(trustedDevice);
    }

    public TrustedDevice findByDeviceIdAndUser_email(String email, String deviceId) {
        return trustedDeviceRepository.findByDeviceIdAndUser_email(email, deviceId)
                .orElseThrow(() -> new EntityNotFoundException("TrustedDevice not found"));
    }

    public boolean existsByDeviceIdAndUser_email(String deviceId, String email) {
        return trustedDeviceRepository.existsByDeviceIdAndUser_Email(deviceId, email);
    }
}
