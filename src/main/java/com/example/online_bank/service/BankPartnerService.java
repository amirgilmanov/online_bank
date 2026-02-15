package com.example.online_bank.service;

import com.example.online_bank.domain.dto.BankPartnerDto;
import com.example.online_bank.domain.entity.Account;
import com.example.online_bank.domain.entity.BankPartner;
import com.example.online_bank.enums.CurrencyCode;
import com.example.online_bank.enums.PartnerCategory;
import com.example.online_bank.repository.AccountRepository;
import com.example.online_bank.repository.BankPartnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.example.online_bank.util.CodeGeneratorUtil.generateAccountNumber;

@Service
@RequiredArgsConstructor
public class BankPartnerService {
    private final AccountRepository accountRepository;
    private final BankPartnerRepository bankPartnerRepository;

    public void create(String name, PartnerCategory category) {
        Account partnerAccount = Account.builder()
                .balance(BigDecimal.ZERO)
                .accountNumber(generateAccountNumber(CurrencyCode.RUB))
                .isBlocked(false)
                .currencyCode(CurrencyCode.RUB)
                .build();

        accountRepository.save(partnerAccount);

        BankPartner bankPartner = BankPartner.builder()
                .name(name)
                .partnerCategory(category)
                .account(partnerAccount)
                .build();
        bankPartnerRepository.save(bankPartner);
        partnerAccount.setBankPartner(bankPartner);
        accountRepository.save(partnerAccount);
    }

    //FIXME захардкодил
    public CurrencyCode getAccountCurrencyCode() {
        return CurrencyCode.RUB;
    }

    public String getAccountNumber(String partnerName) {
        return bankPartnerRepository.findAccountNumberByPartnerName(partnerName).orElseThrow(
                () -> new EntityNotFoundException("Партнер банка не найден")
        );
    }


    public List<BankPartnerDto> getAll() {
        //todo перекинуть на маппер
        return bankPartnerRepository.findAll().stream()
                .map(e -> new BankPartnerDto(e.getName(), e.getPartnerCategory()))
                .toList();
    }
}
