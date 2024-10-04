package com.example.online_bank.repository.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExchangeCurrencyRepository {
    private final Map<String, String> currencyPairs = new HashMap<>();

    public void save(String currencyFrom, String currencyConvertTO) {
        currencyPairs.put(currencyFrom, currencyConvertTO);
    }

    public List<String> findAll() {
        return currencyPairs.values().stream().toList();
    }

    public Optional<String> findByCurrencyFrom(String currencyFrom) {
        return Optional.of(currencyPairs.get(currencyFrom));
    }

    @SuppressWarnings("checkstyle:AvoidNestedBlocks")
    public boolean check(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Ключи и значения не могут быть null");
        }
        if (currencyPairs.containsKey(key) && currencyPairs.get(key).equals(value)) {
            return true;
        }
        if (currencyPairs.containsKey(value) && currencyPairs.get(value).equals(key)) {
            return false;
        }
        throw new RuntimeException("Не удалось найти курс не по значению ни по ключу");
    }
}
