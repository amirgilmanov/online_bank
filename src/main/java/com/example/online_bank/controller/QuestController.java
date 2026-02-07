package com.example.online_bank.controller;

import com.example.online_bank.domain.dto.QuestResponseDto;
import com.example.online_bank.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/quest")
@RequiredArgsConstructor
public class QuestController {
    private final QuestService questService;

    @PostMapping
    public ResponseEntity<List<QuestResponseDto>> createRandomQuest() {
        return ResponseEntity.ok(questService.createRandomQuest());
    }
}
