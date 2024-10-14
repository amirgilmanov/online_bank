package com.example.online_bank.repository.account;

import com.example.online_bank.entity.User;
import com.example.online_bank.entity.account.AccountV2;
import com.example.online_bank.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepository implements CustomRepository<AccountV2> {
    private final Map<String, AccountV2> accountV2Map = new HashMap<>();

    @Override
    public void save(AccountV2 accountV2) {
        accountV2Map.put(accountV2.getId(), accountV2);
    }

    @Override
    public List<AccountV2> findAll() {
        return accountV2Map.values().stream().toList();
    }

    public Optional<AccountV2> findById(String id) {
        return Optional.of(accountV2Map.get(id));
    }

    public List<AccountV2> findByUser(User user) {
        return accountV2Map.values().stream()
                .filter(e -> e.getHolder().equals(user))
                .collect(Collectors.toList());
    }
}
