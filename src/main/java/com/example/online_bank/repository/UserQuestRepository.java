package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.User;
import com.example.online_bank.domain.entity.UserQuest;
import com.example.online_bank.enums.PartnerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {
    Optional<UserQuest> findByUserAndQuest_CategoryAndQuest_DateOfExpiryIsAfter(User user, PartnerCategory category, LocalDate spendPeriod);
}
