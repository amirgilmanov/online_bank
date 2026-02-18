package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.exception.VerificationOtpException;
import com.example.online_bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем %s не найден".formatted(email)));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPasswordHash(), user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).toList());
    }

    /**
     * Устанавливает флаги для пользователя в случае успешной аутентификации
     */
    //Логика верификации
    public void verifyEmailCode(User user, String code, boolean isAuthentificated) throws VerificationOtpException {
        verifiedCodeService.validateCode(user, code, EMAIL, isAuthentificated);
        user.setIsVerified(true);
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
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