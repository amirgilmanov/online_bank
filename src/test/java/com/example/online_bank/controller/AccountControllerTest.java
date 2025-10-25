package com.example.online_bank.controller;

import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.mapper.AccountMapper;
import com.example.online_bank.repository.AccountRepository;
import com.example.online_bank.repository.UserRepository;
import com.example.online_bank.service.AccountService;
import com.example.online_bank.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AccountControllerTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @InjectMocks
    private UserService userService;

    @Test
    void createAccountForUser() {
        //arrange: подготовка данных

        new Account();
    }

    @Test
    void getBalance() {
    }

    @Test
    void findAllByHolder() {
    }
}