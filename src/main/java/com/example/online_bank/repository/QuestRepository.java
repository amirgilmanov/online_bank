package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findAllByDateOfExpiryIsAfter(LocalDate dateOfExpiryAfter);
}
