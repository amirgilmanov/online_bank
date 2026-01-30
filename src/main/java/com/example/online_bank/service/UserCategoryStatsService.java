package com.example.online_bank.service;

import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.repository.UserCategoryStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCategoryStatsService {
    private final UserCategoryStatsRepository userCategoryStatsRepository;

    public void create(UpdateUserStatEvent event){
//        UserCategoryStats userCategoryStats = userCategoryStatsRepository.findByUserAndCategoryAndDate(
//                event.user(),
//                event.partnerCategory(),
//                event.operationDate()
//        ).orElseGet(() -> UserCategoryStats.builder()
//                .category(event.partnerCategory())
//                .user(event.user())
//                .spendPeriod(event.operationDate())
//                .user(event.user())
//                .build()
//        );
//        userCategoryStats.setCountSpendInMonth(userCategoryStats.getCountSpendInMonth() + 1);
//        userCategoryStatsRepository.save(userCategoryStats);
    }

}
