package com.example.online_bank.security;

import com.example.online_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("accountSecurity")
@RequiredArgsConstructor
public class AccountSecurity {
    private final AccountRepository accountRepository;

    public boolean isOwner(String accountNumber, UUID userUuid) {
        return accountRepository.existsByAccountNumberAndHolder_Uuid(accountNumber, userUuid);
    }

    public boolean isOwnsBothAccounts(String accountNumber1, String accountNumber2, UUID userUuid) {
        return accountRepository.existsByAccountNumberAndHolder_Uuid(accountNumber1, userUuid) &&
                accountRepository.existsByAccountNumberAndHolder_Uuid(accountNumber2, userUuid);
    }
}
