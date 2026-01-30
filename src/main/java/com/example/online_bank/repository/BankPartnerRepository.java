package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.BankPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BankPartnerRepository extends JpaRepository<BankPartner, Long> {

    @Query("""
            select bp.account.accountNumber from BankPartner bp
            where bp.name = :partnerName
            """)
    Optional<String> findAccountNumberByPartnerName(String partnerName);
}
