package com.example.online_bank.repository.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ExchangeCurrencyRepositoryV2 {
    private final Map<String, BigDecimal> currencyRateMap = new HashMap<>();

    public void save(String currencyName, BigDecimal rate) {
        currencyRateMap.put(currencyName, rate);
    }

    public List<BigDecimal> findAll() {
        return currencyRateMap.values().stream().toList();
    }

    public BigDecimal findRateByCurrencyName(String currencyName) {
       return currencyRateMap.get(currencyName);
    }
}
