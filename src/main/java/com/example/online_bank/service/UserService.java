package com.example.online_bank.service;

import com.example.online_bank.entity.User;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

    @Transactional
    public void deleteByPhoneNumber(String number) {
        userRepository.deleteByPhoneNumber(number);
    }
}
