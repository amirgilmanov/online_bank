package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.TokenFamily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenFamilyRepository extends JpaRepository<TokenFamily, Long> {
}
