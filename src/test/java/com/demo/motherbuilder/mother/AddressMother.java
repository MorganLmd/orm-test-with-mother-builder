package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.Address;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressMother {

    public static Address lilleAddress() {
        return Address.builder()
                .street("1 rue de la Paix")
                .postalCode("59000")
                .city("Lille")
                .state("Hauts-de-France")
                .country("France")
                .build();
    }

    public static Address parisAddress() {
        return Address.builder()
                .street("42 avenue des Champs-Élysées")
                .postalCode("75008")
                .city("Paris")
                .state("Île-de-France")
                .country("France")
                .build();
    }
}
