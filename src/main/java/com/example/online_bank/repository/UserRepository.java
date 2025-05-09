package com.example.online_bank.repository;

import com.example.online_bank.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByToken(String token);

    boolean existsUserByPhoneNumber(@NonNull String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String number);
}
