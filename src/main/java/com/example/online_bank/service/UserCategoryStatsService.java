package com.example.online_bank.service;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.UserCategoryStats;
import com.example.online_bank.domain.event.UpdateUserStatEvent;
import com.example.online_bank.repository.UserCategoryStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCategoryStatsService {
    private final UserCategoryStatsRepository userCategoryStatsRepository;

    public UserCategoryStats updateUserStat(UpdateUserStatEvent event) {
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
                create(event)
        );

        userCategoryStats.setTotalSpend(userCategoryStats.getTotalSpend().add(event.spendAmount()));
        userCategoryStats.setCountSpendInMonth(userCategoryStats.getCountSpendInMonth() + 1);
        userCategoryStatsRepository.save(userCategoryStats);
        return userCategoryStats;
    }

    private UserCategoryStats create(UpdateUserStatEvent event) {
        return UserCategoryStats.builder()
                .category(event.partnerCategory())
                .user(event.user())
                .spendPeriod(event.operationDate())
                .user(event.user())
                .countSpendInMonth(0)
                .totalSpend(event.spendAmount())
                .build();
    }

    public Optional<UserCategoryStats> findByUserUuid(UUID userUuid) {
        return userCategoryStatsRepository.findByUser_Uuid(userUuid);
    }


    public List<UserCategoryStats> findAllByUser(User user) {
        return userCategoryStatsRepository.findAllByUser(user);
    }
}
