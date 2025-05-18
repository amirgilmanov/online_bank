package com.example.online_bank.testsupport.config;

import com.example.online_bank.repository.UserRepository;
import com.example.online_bank.testsupport.fixture.UserFixtureTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserFixtureTest userTestFixture(UserRepository userRepository) {
        return new UserFixtureTest(userRepository);
    }
}
