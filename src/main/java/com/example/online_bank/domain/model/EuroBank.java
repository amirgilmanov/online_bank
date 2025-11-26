package com.example.online_bank.domain.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("euro-bank")
public class EuroBank extends AbstractBank {

    @Autowired
    public EuroBank(
            @Value("${euro-bank.bank.name}")
            String name,
            @Value("${euro-bank.bank.partner.prefix-url}")
            String prefixUrl
    ) {
        super(name, prefixUrl);
    }
}
