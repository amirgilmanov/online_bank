package com.example.online_bank.service;

import com.example.online_bank.mapper.UserMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AuthenticationServiceTest {
    @Mock
    UserService userService;
    @InjectMocks
    AuthenticationService authenticationService;
    @Mock
    VerifiedCodeService verifiedCodeService;
    @Mock
    UserMapper userMapper;
    @Mock
    TokenService tokenService;
    //нет с таким email
    //
    //
    //
    //
    //

}