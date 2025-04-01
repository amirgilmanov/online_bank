package com.example.online_bank.repository;

import com.example.online_bank.entity.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("select op from Operation op where op.account.accountNumber = :accountNumber")
    List<Operation> findByAccountNumber(@Param("accountNumber") String accountNumber);

    List<Operation> findAllByAccount_AccountNumber(String accountNumber, PageRequest pageRequest);
}
