package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.model.CustomUserDetails;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final VerifiedCodeService verifiedCodeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем %s не найден".formatted(username)));
        return new CustomUserDetails(user);
    }

    public boolean verifyEmailCode(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %s не найден".formatted(userId)));

        boolean isValid = verifiedCodeService.validateCode(user, code, EMAIL);
        if (isValid) {
            user.setIsVerified(true);
            userRepository.save(user);
        }
        return isValid;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    public boolean existsByPhoneNumber(String number) {
        return userRepository.existsUserByPhoneNumber(number);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUuid(UUID userUuid) {
        return userRepository.findByUuid(userUuid);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional()
    public void deleteByPhoneNumber(String number) {
        userRepository.deleteByPhoneNumber(number);
    }
}