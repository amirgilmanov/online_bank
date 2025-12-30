package com.example.online_bank.controller;

import com.example.online_bank.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final NotificationService notificationService;
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.status(201).body("test");
    }

    @GetMapping("/pure")
    public void pureJava(HttpServletResponse response) throws IOException {
        response.getWriter().write("PURE-JAVA-WORKS");
        response.getWriter().flush();
    }

    @PostMapping("/send-email")
    public void sendEmail(@RequestParam String email) {
        notificationService.sendOtpCode(email, "HELLO WORLD");
    }
}
