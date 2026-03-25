package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.Price;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PriceMother {

    private final Price.PriceBuilder priceBuilder;

    // ------------------------------------------------------------------ Object Mother

    public static PriceMother euros(double value) {
        return new PriceMother(
                Price.builder()
                        .value(BigDecimal.valueOf(value))
                        .currency(CurrencyMother.euro())
        );
    }

    public static PriceMother dollars(double value) {
        return new PriceMother(
                Price.builder()
                        .value(BigDecimal.valueOf(value))
                        .currency(CurrencyMother.dollar())
        );
    }

    // ------------------------------------------------------------------ Test Data Builder

    public PriceMother withValue(BigDecimal value) {
        this.priceBuilder.value(value);
        return this;
    }

    // ------------------------------------------------------------------ final build

    public Price build() {
        return priceBuilder.build();
    }
}

