package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * BaseCurrency - Валюта, от которой происходит обмен.
 * TargetCurrency - Валюта, к которой происходит обмен
 * ExchangeRate - Курс обмена.
 * Пример записи - доллар/рубль 90. Это значит, что один доллар стоит 90 рублей
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column
    @Enumerated(STRING)
    private CurrencyCode baseCurrency;     // Валюта, от которой происходит обмен(USD)
    @Column
    @Enumerated(STRING)
    private CurrencyCode targetCurrency;   // Валюта, к которой происходит обмен(RUB)
    @Column
    private BigDecimal rate; // цена котируемой валюты по отношению к базовой
}
