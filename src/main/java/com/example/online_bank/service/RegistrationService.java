package com.example.online_bank.service;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.dto.SignUpDtoResponse;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.enums.VerifiedCodeType;
import com.example.online_bank.exception.EntityAlreadyExistsException;
import com.example.online_bank.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final VerifiedCodeService verifiedCodeService;

    @Transactional
    public SignUpDtoResponse signUp(RegistrationDto registrationDto) {
        if (userService.existsByPhoneNumber(registrationDto.phone()) ||
                userService.existsByEmail(registrationDto.email())) {
            log.warn("Номер уже используется");
            throw new EntityAlreadyExistsException("Пользователь с таким номером или почтой уже зарегистрирован");
        }

        User user = userMapper.toUser(registrationDto, bCryptPasswordEncoder, roleService);
        userService.save(user);

        String code = verifiedCodeService.generateVerifiedCode();
        LocalDateTime expireDate = verifiedCodeService.createExpirationDate(200);
        verifiedCodeService.createAndSave(code, user, expireDate, VerifiedCodeType.EMAIL);

        return new SignUpDtoResponse(registrationDto.email(), code);
    }
}
