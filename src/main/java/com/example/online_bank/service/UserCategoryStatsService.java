package com.example.online_bank.service;

import com.example.online_bank.domain.entity.UserCategoryStats;
import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.repository.UserCategoryStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class UserCategoryStatsService {
    private final UserCategoryStatsRepository userCategoryStatsRepository;

    public UserCategoryStats updateUserStat(UpdateUserStatEvent event){
        Month month = event.operationDate().getMonth();
        YearMonth yearMonth = YearMonth.of(event.operationDate().getYear(), event.operationDate().getMonthValue());
        LocalDate startDate = LocalDate.of(event.operationDate().getYear(), month, 1);
        LocalDate endDate = LocalDate.of(event.operationDate().getYear(), month, yearMonth.lengthOfMonth());

        UserCategoryStats userCategoryStats = userCategoryStatsRepository.findByUserAndCategoryAndSpendPeriodBetween(
                event.user(),
                event.partnerCategory(),
                startDate,
                endDate
        ).orElseGet(() ->
                UserCategoryStats.builder()
                        .category(event.partnerCategory())
                        .user(event.user())
                        .spendPeriod(event.operationDate())
                        .user(event.user())
                        .countSpendInMonth(0)
                        .totalSpend(event.spendAmount())
                        .build()
        );

        userCategoryStats.setTotalSpend(userCategoryStats.getTotalSpend().add(event.spendAmount()));
        userCategoryStats.setCountSpendInMonth(userCategoryStats.getCountSpendInMonth() + 1);
        userCategoryStatsRepository.save(userCategoryStats);
        return  userCategoryStats;
    }

}
