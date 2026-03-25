package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.Order;
import com.demo.motherbuilder.entity.OrderItem;
import com.demo.motherbuilder.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMother {

    // ------------------------------------------------------------------ Constants

    public static final String STANDARD_ORDER_REFERENCE = "CMD-STANDARD-001";
    public static final String DRAFT_ORDER_REFERENCE = "CMD-DRAFT-001";
    public static final String SHIPPED_ORDER_REFERENCE = "CMD-SHIPPED-001";

    // ------------------------------------------------------------------ Builder

    private final Order.OrderBuilder orderBuilder;
    private final List<OrderItem> items = new ArrayList<>();

    // ------------------------------------------------------------------ Object Mother


    public static OrderMother standardOrder() {
        return new OrderMother(
                Order.builder()
                        .orderReference(STANDARD_ORDER_REFERENCE)
                        .status(OrderStatus.VALIDATED)
                        .customer(CustomerMother.defaultCustomer().build())
                        .paymentReference(PaymentReferenceMother.pendingPayment().build())
        );
    }

    public static OrderMother draftOrder() {
        return new OrderMother(
                Order.builder()
                        .orderReference(DRAFT_ORDER_REFERENCE)
                        .status(OrderStatus.DRAFT)
                        .customer(CustomerMother.defaultCustomer().build())
        );
    }

    public static OrderMother shippedOrder() {
        OrderMother mother = new OrderMother(
                Order.builder()
                        .orderReference(SHIPPED_ORDER_REFERENCE)
                        .status(OrderStatus.SHIPPED)
                        .customer(CustomerMother.businessCustomer().build())
                        .paymentReference(PaymentReferenceMother.successfulPayment().build())
        );
        mother.items.add(OrderItemMother.defaultItem().withQuantity(2).build());
        mother.items.add(OrderItemMother.serviceItem().build());
        return mother;
    }

    // ------------------------------------------------------------------ Test Data Builder

    public OrderMother withOrderReference(String orderReference) {
        this.orderBuilder.orderReference(orderReference);
        return this;
    }

    public OrderMother withStatus(OrderStatus status) {
        this.orderBuilder.status(status);
        return this;
    }

    public OrderMother withCustomer(Function<CustomerMother, CustomerMother> customerFn) {
        this.orderBuilder.customer(customerFn.apply(CustomerMother.defaultCustomer()).build());
        return this;
    }

    public OrderMother withPaymentReference(Function<PaymentReferenceMother, PaymentReferenceMother> paymentFn) {
        this.orderBuilder.paymentReference(paymentFn.apply(PaymentReferenceMother.pendingPayment()).build());
        return this;
    }

    public OrderMother withOrderItem(Function<OrderItemMother, OrderItemMother> itemFn) {
        this.items.add(itemFn.apply(OrderItemMother.productItem()).build());
        return this;
    }

    public OrderMother withPackageItem(Function<OrderItemMother, OrderItemMother> itemFn) {
        this.items.add(itemFn.apply(OrderItemMother.packageItem()).build());
        return this;
    }

    // --- EXPRESSIVE DOMAIN METHODS (Semantic Alternative) ---

    public OrderMother withShippedOrder() {
        this.orderBuilder.status(OrderStatus.SHIPPED);
        return this;
    }


    public OrderMother withBusinessCustomer() {
        this.orderBuilder.customer(CustomerMother.businessCustomer().build());
        return this;
    }

    public OrderMother withoutPaymentReference() {
        this.orderBuilder.paymentReference(null);
        return this;
    }

    public OrderMother withoutItems() {
        this.items.clear();
        return this;
    }


    // ------------------------------------------------------------------ final build

    public Order build() {
        return orderBuilder
                .items(new ArrayList<>(this.items))
                .build();
    }
}
