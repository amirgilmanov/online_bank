package com.example.online_bank.testsupport.fixture;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.entity.User;
import com.example.online_bank.mapper.UserMapper;
import com.example.online_bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor()
public class UserFixtureTest {
    private final UserRepository userRepository;

    public User createTestUser() {
        String testToken = "test-qwe1234-token";
        String testName = "testName";
        String testSurname = "testSurname";
        String phoneNumber = generatePhoneNumber();

        User user = new User();
        user.setName(testName);
        user.setSurname(testSurname);
        user.setPhoneNumber(phoneNumber);
        user.setToken(testToken);
        return userRepository.save(user);
    }

    private String generatePhoneNumber() {

        return "+7999000" + new Random().nextInt(10000);
    }
}
