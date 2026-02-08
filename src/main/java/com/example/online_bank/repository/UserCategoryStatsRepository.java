package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.UserCategoryStats;
import com.example.online_bank.enums.PartnerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserCategoryStatsRepository extends JpaRepository<UserCategoryStats, Long> {
    Optional<UserCategoryStats> findByUserAndCategoryAndSpendPeriodBetween(User user, PartnerCategory category, LocalDate start, LocalDate end);
}
