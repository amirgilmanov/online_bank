package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.BankPartner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankPartnerRepository extends JpaRepository<BankPartner, Long> {
}
