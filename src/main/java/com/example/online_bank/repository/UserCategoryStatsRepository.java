package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.UserCategoryStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryStatsRepository extends JpaRepository<UserCategoryStats, Long> {
   // Optional<UserCategoryStats> findByUserAndCategoryAndDate(User user, PartnerCategory category, LocalDate date);
}
