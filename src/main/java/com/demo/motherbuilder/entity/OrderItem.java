package com.demo.motherbuilder.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Determines what this item is and what it may contain:
     * <ul>
     *   <li>{@link ItemType#PRODUCT} – leaf node, no children</li>
     *   <li>{@link ItemType#SERVICE} – may contain {@code PRODUCT} children</li>
     *   <li>{@link ItemType#PACKAGE} – may contain {@code PACKAGE}, {@code SERVICE} or {@code PRODUCT} children</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String sku;
    private String productReference;
    private int quantity;

    @Embedded
    private Price price;

    /**
     * Nested items owned by this item.
     * The FK {@code parent_item_id} sits on the child row in {@code order_items}.
     * Top-level items (directly attached to an {@link Order}) have this column set to {@code null}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_item_id")
    private List<OrderItem> children;
}
