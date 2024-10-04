package com.example.online_bank.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserAuthenticationRepository {
    private final Map<String, String> idAndPinCode = new HashMap<>();

    public void save(String pinCode, String id) {
        idAndPinCode.put(pinCode, id);
    }

    public boolean findByPinCode(String pinCode) {
        return idAndPinCode.containsKey(pinCode);
    }
}
