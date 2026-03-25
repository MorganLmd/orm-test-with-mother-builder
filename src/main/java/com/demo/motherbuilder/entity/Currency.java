package com.demo.motherbuilder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Column(name = "currency_code", length = 3)
    private String code;

    @Column(name = "currency_symbol", length = 5)
    private String symbol;

    @Column(name = "currency_name")
    private String name;
}

