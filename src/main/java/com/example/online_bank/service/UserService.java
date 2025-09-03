package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.model.CustomUserDetails;
import com.example.online_bank.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final VerifiedCodeService verifiedCodeService;

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем %s не найден".formatted(username)));
        return new CustomUserDetails(user);
    }

    Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
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

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean verifyEmailCode(String email, String code) {
        User user = findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с почтой %s не найден".formatted(email)));

        boolean isValid = verifiedCodeService.validateCode(user, code, EMAIL);
        if (isValid) {
            user.setIsVerified(true);
            userRepository.save(user);
        }
        return isValid;
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

    public Collection<SimpleGrantedAuthority> toSimpleGrantedAuthority(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

}

//TODO сделать подтверждение номера и почты

