package com.example.online_bank.service;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.entity.User;
import com.example.online_bank.exception.EntityAlreadyExistsException;
import com.example.online_bank.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.online_bank.util.CodeGeneratorUtil.generatePinCode;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final UserService userService;
    private final AuthService authenticationService;
    private final UserMapper userMapper;

    @Transactional
    public String signUp(SignUpDto signUpDto) {
        if (userService.existsByPhoneNumber(signUpDto.phone())) {
            throw new EntityAlreadyExistsException("пользователь с таким номером уже зарегистрирован");
        }
        User user = userMapper.convertDtoToUser(signUpDto);

        String pinCode = generatePinCode();
        user.setPinCode(pinCode);
        userService.save(user);

        authenticationService.save(pinCode, user);
        return pinCode;
    }
}
