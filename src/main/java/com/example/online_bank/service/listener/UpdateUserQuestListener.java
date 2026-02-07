package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.UpdateUserQuestEvent;
import com.example.online_bank.repository.QuestRepository;
import com.example.online_bank.repository.UserQuestRepository;
import com.example.online_bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserQuestListener {
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final UserQuestRepository userQuestRepository;
    @EventListener
    @Async
    public void handle(UpdateUserQuestEvent event) {
        //надо сравнить прогресс в userQuest с ивентом и если равно, то ставим галочку что квест выполнен у конкретного пользователя и найти квест и очки перекинуть на новую ентити
        userQuestRepository.findByUserAndCategory
    }
}
