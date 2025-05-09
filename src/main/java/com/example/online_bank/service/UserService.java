package com.example.online_bank.service;

import com.example.online_bank.entity.User;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findByToken(String token) {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким токеном не найден"));
    }

    public boolean existsByPhoneNumber(String number) {
        return userRepository.existsUserByPhoneNumber(number);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional()
    public void deleteByPhoneNumber(String number) {
        userRepository.deleteByPhoneNumber(number);
    }

    public String findPinCodeByPhoneNumber(String number) {
        log.info("Достаем пин-код по номеру телефона");
        return userRepository.findByPhoneNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким номером не найден")).getPinCode();
    }
}
