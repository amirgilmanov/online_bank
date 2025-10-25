package com.example.online_bank.service;

import com.example.online_bank.mapper.UserMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private RoleService roleService;
    @Mock
    private VerifiedCodeService verifiedCodeService;
    @InjectMocks
    private RegistrationService registrationService;

//    @Test
//    void successSignUp() {
//        //arrange Подготовка данных
//        RegistrationDto registrationDto = new RegistrationDto(
//                "testName",
//                "testSurname",
//                "testPatronymic",
//                "89608052696",
//                "wass",
//                "myemail@.com"
//        );
//
//        OngoingStubbing<User> user = Mockito.when(userMapper.toUser(registrationDto, roleService, bCryptPasswordEncoder)).thenReturn(
//                User.builder()
//                        .name(registrationDto.name())
//                        .surname(registrationDto.surname())
//                        .patronymic(registrationDto.patronymic())
//                        .email(registrationDto.email())
//                        .passwordHash(bCryptPasswordEncoder.encode(registrationDto.password()))
//                        .phoneNumber(registrationDto.phone())
//                        .id(1L)
//                        .roles(List.of(new Role(1L, "ROLE_USER")))
//                        .isBlocked(false)
//                        .failedAttempts(0)
//                        .isVerified(false)
//                        .build());
//        Mockito.when(userService.save()).
//
//
//    }

}