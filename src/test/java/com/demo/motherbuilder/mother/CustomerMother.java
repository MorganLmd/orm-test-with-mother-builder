package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.Customer;
import com.demo.motherbuilder.entity.CustomerType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerMother {

    private final Customer.CustomerBuilder customerBuilder;

    // ------------------------------------------------------------------ Object Mother

    public static CustomerMother defaultCustomer() {
        return new CustomerMother(
                Customer.builder()
                        .customerType(CustomerType.INDIVIDUAL)
                        .firstName("Jean")
                        .lastName("Dupont")
                        .birthDate(LocalDate.of(1985, 6, 15))
                        .email("jean.dupont@example.com")
                        .phoneNumber("+33612345678")
                        .address(AddressMother.lilleAddress())
        );
    }

    public static CustomerMother businessCustomer() {
        return new CustomerMother(
                Customer.builder()
                        .customerType(CustomerType.BUSINESS)
                        .firstName("Marie")
                        .lastName("Curie")
                        .birthDate(LocalDate.of(1867, 11, 7))
                        .email("marie.curie@example.com")
                        .phoneNumber("+33698765432")
                        .address(AddressMother.parisAddress())
        );
    }

    // ------------------------------------------------------------------ Test Data Builder

    public CustomerMother withFirstName(String firstName) {
        this.customerBuilder.firstName(firstName);
        return this;
    }

    public CustomerMother withEmail(String email) {
        this.customerBuilder.email(email);
        return this;
    }

    // ------------------------------------------------------------------ Terminaison

    public Customer build() {
        return customerBuilder.build();
    }
}
