package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAllByHolder(User holder);

    boolean existsByHolder_Token(String token);

    boolean existsByAccountNumber(String accountNumber);
}
