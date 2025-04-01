package com.example.online_bank.repository;

import com.example.online_bank.entity.ExchangeRate;
import com.example.online_bank.enums.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeCurrencyRepository extends JpaRepository<ExchangeRate, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    seleсt id, base_currency, target_currency, rate
                    from exchange_rate
                    where base_currency = :baseCurrency
                    and target_currency = :targetCurrency
                    """)
    Optional<ExchangeRate> findCurrencyRate(
            @Param("baseCurrency")
            CurrencyCode baseCurrency,
            @Param("targetCurrency")
            CurrencyCode targetCurrency
    );

    boolean existsByBaseCurrencyAndTargetCurrency(CurrencyCode baseCurrency, CurrencyCode targetCurrency);
}
