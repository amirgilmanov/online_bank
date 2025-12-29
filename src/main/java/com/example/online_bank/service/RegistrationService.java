package com.example.online_bank.service;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.dto.RegistrationDtoResponse;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.VerifiedCode;
import com.example.online_bank.exception.EntityAlreadyExistsException;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.util.CodeGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.online_bank.enums.VerifiedCodeType.EMAIL;

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
    public RegistrationDtoResponse signUp(RegistrationDto registrationDto) {
        return register(registrationDto, userMapper::toUser);
    }

    @Transactional
    public RegistrationDtoResponse adminSignUp(RegistrationDto registrationDto) {
        return register(registrationDto, userMapper::toUserAdmin);
    }

    public RegistrationDtoResponse register(
            RegistrationDto registrationDto,
            TriFunction<RegistrationDto, RoleService, BCryptPasswordEncoder, User> mapper) {

        if (userService.existsByPhoneNumber(registrationDto.phone()) ||
                userService.existsByEmail(registrationDto.email())) {
            log.warn("Номер или почта уже используется");
            throw new EntityAlreadyExistsException("Пользователь с таким номером или почтой уже зарегистрирован");
        }

        User user = mapper.apply(registrationDto, roleService, bCryptPasswordEncoder);
        userService.save(user);

        String code = CodeGeneratorUtil.generateOtp();
        LocalDateTime expireDate = verifiedCodeService.createExpirationDate(200);
        VerifiedCode verifiedCode = verifiedCodeService.createVerifiedCode(code, user, expireDate, EMAIL);
        verifiedCodeService.save(verifiedCode);

        return userMapper.toRegistrationDtoResponse(registrationDto, code);
    }
}