package com.example.online_bank.service.listener;

import com.example.online_bank.domain.entity.UserCategoryStats;
import com.example.online_bank.domain.event.UpdateUserQuestEvent;
import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.service.UserCategoryStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UpdateUserStatEventListener {
    private final UserCategoryStatsService userCategoryStatsService;
    private final ApplicationEventPublisher applicationEventPublisher;
    @EventListener
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateUserStat(UpdateUserStatEvent event) {
        UserCategoryStats userCategoryStats = userCategoryStatsService.updateUserStat(event);
        UpdateUserQuestEvent updateUserQuestEvent = new UpdateUserQuestEvent(
                userCategoryStats.getCountSpendInMonth(),
                userCategoryStats.getCategory(),
                userCategoryStats.getUser(),
                userCategoryStats.getSpendPeriod(),
                event.userAccount()
        );
        applicationEventPublisher.publishEvent(updateUserQuestEvent);
    }
}
