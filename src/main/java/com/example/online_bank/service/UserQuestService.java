package com.example.online_bank.service;

import com.example.online_bank.domain.entity.Quest;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.UserQuest;
import com.example.online_bank.repository.UserQuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQuestService {
    private final UserQuestRepository userQuestRepository;

    public void create(Quest quest, User user) {
        UserQuest.builder()
                .quest(quest)
                .user(user)
                .build();
    }

    public void saveAll(List<UserQuest> userQuests) {
        userQuestRepository.saveAll(userQuests);
    }
}
