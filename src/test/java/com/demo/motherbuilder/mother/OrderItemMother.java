package com.demo.motherbuilder.mother;

import com.demo.motherbuilder.entity.ItemType;
import com.demo.motherbuilder.entity.OrderItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemMother {

    // ------------------------------------------------------------------ Constants

    public static final String DEFAULT_PRODUCT_SKU = "SKU-001";
    public static final String DEFAULT_PRODUCT_REFERENCE = "PROD-001";

    public static final String DEFAULT_SERVICE_SKU = "SVC-001";
    public static final String DEFAULT_SERVICE_REFERENCE = "SERVICE-INSTALLATION";

    public static final String DEFAULT_PACKAGE_SKU = "PKG-001";
    public static final String DEFAULT_PACKAGE_REFERENCE = "PACKAGE-STARTER";

    public static final String PREMIUM_PRODUCT_SKU = "SKU-PREMIUM-001";
    public static final String PREMIUM_PRODUCT_REFERENCE = "PROD-PREMIUM-001";

    // ------------------------------------------------------------------ Builder

    private final OrderItem.OrderItemBuilder orderItemBuilder;
    private final List<OrderItem> children = new ArrayList<>();

    // ------------------------------------------------------------------ Object Mother

    public static OrderItemMother productItem() {
        return new OrderItemMother(
                OrderItem.builder()
                        .itemType(ItemType.PRODUCT)
                        .sku(DEFAULT_PRODUCT_SKU)
                        .productReference(DEFAULT_PRODUCT_REFERENCE)
                        .quantity(1)
                        .price(PriceMother.euros(29.99).build())
        );
    }

    public static OrderItemMother serviceItem() {
        return new OrderItemMother(
                OrderItem.builder()
                        .itemType(ItemType.SERVICE)
                        .sku(DEFAULT_SERVICE_SKU)
                        .productReference(DEFAULT_SERVICE_REFERENCE)
                        .quantity(1)
                        .price(PriceMother.euros(99.99).build())
        );
    }

    public static OrderItemMother packageItem() {
        return new OrderItemMother(
                OrderItem.builder()
                        .itemType(ItemType.PACKAGE)
                        .sku(DEFAULT_PACKAGE_SKU)
                        .productReference(DEFAULT_PACKAGE_REFERENCE)
                        .quantity(1)
                        .price(PriceMother.euros(249.99).build())
        );
    }

    public static OrderItemMother defaultItem() {
        return productItem();
    }

    // ------------------------------------------------------------------ Test Data Builder

    public OrderItemMother withItemType(ItemType itemType) {
        this.orderItemBuilder.itemType(itemType);
        return this;
    }

    public OrderItemMother withSku(String sku) {
        this.orderItemBuilder.sku(sku);
        return this;
    }

    public OrderItemMother withQuantity(int quantity) {
        this.orderItemBuilder.quantity(quantity);
        return this;
    }

    /**
     * Adds a child item to this item (meaningful for SERVICE and PACKAGE types).
     * Defaults to a {@link #productItem()} when the lambda starts from scratch.
     */
    public OrderItemMother withChild(Function<OrderItemMother, OrderItemMother> childFn) {
        this.children.add(childFn.apply(OrderItemMother.productItem()).build());
        return this;
    }

    // ------------------------------------------------------------------ final build

    public OrderItem build() {
        return orderItemBuilder
                .children(new ArrayList<>(this.children))
                .build();
    }
}
