package com.example.online_bank.domain.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("money-bank")
@Component
public class MoneyBank extends AbstractBank {

    @Autowired
    public MoneyBank(
            @Value("${money-bank.bank.name}")
            String name,
            @Value("${money-bank.bank.partner.url}")
            String url
    ) {
        super(name, url);
    }
}
