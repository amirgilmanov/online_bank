package com.example.online_bank.entity;

import com.example.online_bank.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.ORDINAL;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * BaseCurrency - Валюта, от которой происходит обмен
 * TargetCurrency - Валюта, к которой происходит обмен
 * ExchangeRate - Курс обмена.
 * Пример записи - доллар/рубль 90. Это значит, что один доллар стоит 90 рублей
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column
    @Enumerated(ORDINAL)
    private CurrencyCode baseCurrency;     // Валюта, от которой происходит обмен(USD)
    @Column
    @Enumerated(ORDINAL)
    private CurrencyCode targetCurrency;   // Валюта, к которой происходит обмен(RUB)
    @Column
    private BigDecimal rate; // цена котируемой валюты по отношению к базовой
}
