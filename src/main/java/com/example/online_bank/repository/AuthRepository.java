package com.example.online_bank.repository;

import com.example.online_bank.entity.AuthUsers;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUsers, Long> {

    boolean existsByPinCode(String pinCode);

    boolean existsByUser_PhoneNumber(@NonNull String userPhoneNumber);

    Optional<AuthUsers> findByPinCode(String pinCode);

    Optional<AuthUsers> findByUser_PhoneNumber(@NonNull String userPhoneNumber);
}
