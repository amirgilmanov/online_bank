package com.example.online_bank.repository;

import com.example.online_bank.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByToken(String token);

    boolean existsUserByPhoneNumber(@NonNull String phoneNumber);

    @Modifying
    void deleteByPhoneNumber(@NonNull String phoneNumber);
}
