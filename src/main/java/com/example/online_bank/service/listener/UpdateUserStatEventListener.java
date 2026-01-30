package com.example.online_bank.service.listener;

import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.service.UserCategoryStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserStatEventListener {
    private final UserCategoryStatsService userCategoryStatsService;

    @EventListener
    @Async
    public void updateUserStat(UpdateUserStatEvent event) {
        userCategoryStatsService.create(event);
    }
}
