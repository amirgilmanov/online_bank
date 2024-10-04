package com.example.online_bank.repository.currency;

import com.example.online_bank.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExchangeCurrencyRepositoryV3 {
    private final Map<String, ExchangeRate> exchangeRateMap = new HashMap<>();

    public void save(String currenciesRate, ExchangeRate exchangeRateInfo) {
        exchangeRateMap.put(currenciesRate, exchangeRateInfo);
    }

    public List<ExchangeRate> findAll() {
        return exchangeRateMap.values().stream().toList();
    }

    public Optional<ExchangeRate> findByCurrencyPairs(String currenciesPair) {
        return Optional.ofNullable(exchangeRateMap.get(currenciesPair));
    }

    public boolean containsPair(String pair) {
        return exchangeRateMap.containsKey(pair);
    }
}
