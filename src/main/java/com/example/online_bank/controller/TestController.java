package com.example.online_bank.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("test")
@NoArgsConstructor
public class TestController {
    @GetMapping
    public ResponseEntity<String>  test(){
        return ResponseEntity.status(201).body("test");
    }

    @GetMapping("/pure")
    public void pureJava(HttpServletResponse response) throws IOException {
        response.getWriter().write("PURE-JAVA-WORKS");
        response.getWriter().flush();
    }
}
