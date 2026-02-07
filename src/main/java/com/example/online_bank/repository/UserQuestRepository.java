package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestRepository extends JpaRepository<UserQuest, Long> {
}
