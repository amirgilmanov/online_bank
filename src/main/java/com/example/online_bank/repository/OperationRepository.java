package com.example.online_bank.repository;

import com.example.online_bank.entity.Account;
import com.example.online_bank.entity.Operation;
import com.example.online_bank.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByAccount_AccountNumber(String accountNumber, Pageable pageRequest);

    List<Operation> findByAccount(Account account, Pageable pageable);

    List<Operation> findAllByAccount_Holder(User holder, Pageable pageable);
}
