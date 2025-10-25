package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByAccount_AccountNumber(String accountNumber, Pageable pageRequest);

    List<Operation> findAllByAccount_Holder_Uuid(UUID uuid, Pageable pageable);
}
