package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.Currency;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyMother {

    public static Currency euro() {
        return Currency.builder()
                .code("EUR")
                .symbol("€")
                .name("Euro")
                .build();
    }

    public static Currency dollar() {
        return Currency.builder()
                .code("USD")
                .symbol("$")
                .name("US Dollar")
                .build();
    }

    public static Currency poundSterling() {
        return Currency.builder()
                .code("GBP")
                .symbol("£")
                .name("Pound Sterling")
                .build();
    }
}
