package com.example.online_bank.service.listener;

import com.example.online_bank.domain.entity.UserQuest;
import com.example.online_bank.domain.event.UpdateBonusAccountEvent;
import com.example.online_bank.domain.event.UpdateUserQuestEvent;
import com.example.online_bank.repository.UserQuestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserQuestListener {
    private final UserQuestRepository userQuestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    @Async
    public void handle(UpdateUserQuestEvent event) {
        //надо сравнить прогресс в userQuest с ивентом и если равно,
        // то ставим галочку, что квест выполнен у конкретного пользователя и найти квест и очки перекинуть на новую ентити
        UserQuest userQuest = userQuestRepository.findByUserAndQuest_CategoryAndQuest_DateOfExpiryIsAfter(
                event.user(),
                event.category(),
                event.spendPeriod()
        ).orElseThrow(() -> new EntityNotFoundException("Прогресс по данным квестам не найден"));

        log.info("Прогресс пользователя {}", event.userProgress());
        log.info("Прогресс пользователя в квесте {}", userQuest.getUserProgress());

        if (event.userProgress() >= userQuest.getQuest().getProgress()) {
            userQuest.setIsComplete(true);
            log.info("Квест выполнен {}", userQuest);
            userQuestRepository.save(userQuest);

            Integer points = userQuest.getQuest().getPointReward();
            String userAccountNumber = event.userAccountNumber();
            eventPublisher.publishEvent(new UpdateBonusAccountEvent(points, userAccountNumber));
            log.info("Обновление бонусного счета у пользователя {}", userAccountNumber);
        }
    }
}
