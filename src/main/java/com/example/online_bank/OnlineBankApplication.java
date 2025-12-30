package com.example.online_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class OnlineBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBankApplication.class, args);
    }
}
