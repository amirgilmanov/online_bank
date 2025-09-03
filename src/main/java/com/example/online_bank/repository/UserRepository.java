package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsUserByPhoneNumber(@NonNull String phoneNumber);

    @Modifying
    void deleteByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String number);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
