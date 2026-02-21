package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillDb {
    private final QuestService questService;
    private final UserQuestService userQuestService;
    private final UserService userService;

    @PostConstruct
    private void fillQuest(){
        questService.createRandomQuest();
        User user = userService.findByEmail("gilmanovamir19@gmail.com").orElseThrow(RuntimeException::new);
        userQuestService.makeRelationBetweenUserAndQuest(user);
    }
}
