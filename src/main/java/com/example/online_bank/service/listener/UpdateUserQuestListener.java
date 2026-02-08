package com.example.online_bank.service.listener;

import com.example.online_bank.domain.entity.UserQuest;
import com.example.online_bank.domain.event.UpdateBonusAccountEvent;
import com.example.online_bank.domain.event.UpdateUserQuestEvent;
import com.example.online_bank.repository.UserQuestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UpdateUserQuestListener {
    private final UserQuestRepository userQuestRepository;
    private final ApplicationEventPublisher eventPublisher;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(UpdateUserQuestEvent event) {
        //надо сравнить прогресс в userQuest с ивентом и если равно,
        // то ставим галочку, что квест выполнен у конкретного пользователя и найти квест и очки перекинуть на новую ентити
        UserQuest userQuest = userQuestRepository.findByUserAndQuest_CategoryAndQuest_DateOfExpiryIsAfter(event.user(), event.category(), event.spendPeriod())
                .orElseThrow(()-> new EntityNotFoundException("Прогресс по данным квестам не найден"));
        if (event.userProgress().compareTo(userQuest.getUserProgress()) == 0 ){
            userQuest.setIsComplete(true);
            userQuestRepository.save(userQuest);

            Integer points = userQuest.getQuest().getPointReward();
            String userAccountNumber = event.userAccountNumber();
            eventPublisher.publishEvent(new UpdateBonusAccountEvent(points, userAccountNumber));
        }
    }
}
