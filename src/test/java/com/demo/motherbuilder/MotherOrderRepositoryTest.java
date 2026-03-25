package com.demo.motherbuilder;

import com.demo.motherbuilder.entity.CustomerType;
import com.demo.motherbuilder.entity.ItemType;
import com.demo.motherbuilder.entity.Order;
import com.demo.motherbuilder.entity.OrderItem;
import com.demo.motherbuilder.entity.OrderStatus;
import com.demo.motherbuilder.entity.PaymentStatus;
import com.demo.motherbuilder.mother.OrderMother;
import com.demo.motherbuilder.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class MotherOrderRepositoryTest {

    private static final int EXPECTED_TOP_LEVEL_ITEMS_SCENARIO_1 = 2;
    private static final int EXPECTED_CHILDREN_IN_PACKAGE = 1;

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Demonstrate simple 'with' usage (overriding values)")
    void shouldCreateOrderUsingSimpleWith() {
        // GIVEN: Simple value override via fluent API
        Order orderToSave = OrderMother.standardOrder()
                .withOrderReference("CUSTOM-REF-123")
                .withStatus(OrderStatus.DRAFT)
                .build();

        // WHEN
        Order saved = orderRepository.save(orderToSave);

        entityManager.flush();
        entityManager.clear();

        // THEN: Verify roundtrip from DB
        Order reloaded = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getOrderReference()).isEqualTo("CUSTOM-REF-123");
        assertThat(reloaded.getStatus()).isEqualTo(OrderStatus.DRAFT);
    }

    @Test
    @DisplayName("Demonstrate 'with' using Functions (lambdas for nested objects)")
    void shouldCreateOrderUsingLambdaCustomizers() {
        // GIVEN: Customizing deep parts of the aggregate without technical noise
        Order orderToSave = OrderMother.standardOrder()
                .withCustomer(c -> c.withFirstName("Alice").withEmail("alice@test.com"))
                .withOrderItem(i -> i.withQuantity(10).withSku("BULK-SKU"))
                .build();

        // WHEN
        Order saved = orderRepository.save(orderToSave);

        entityManager.flush();
        entityManager.clear();

        // THEN
        Order reloaded = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getCustomer().getFirstName()).isEqualTo("Alice");
        assertThat(reloaded.getItems()).hasSize(1);
        assertThat(reloaded.getItems().getFirst().getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("Demonstrate 'with' using Functions for complexe items with children (lambdas for nested objects)")
    void shouldSaveOrderWithDependentItems() {

        Order orderToSave = OrderMother.standardOrder()
                .withPaymentReference(paymentReferenceMother -> paymentReferenceMother.withStatus(PaymentStatus.SUCCESS))
                .withPackageItem(parentPackageItem -> parentPackageItem
                        .withChild(childProductItem -> childProductItem.withItemType(ItemType.PRODUCT)))
                .withOrderItem(item -> item.withItemType(ItemType.PRODUCT))
                .build();

        Order savedOrder = orderRepository.save(orderToSave);

        Order order = orderRepository.findById(savedOrder.getId()).orElseThrow();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.VALIDATED);
        assertThat(order.getCustomer().getCustomerType()).isEqualTo(CustomerType.INDIVIDUAL);
        assertThat(order.getPaymentReference().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(order.getItems()).hasSize(EXPECTED_TOP_LEVEL_ITEMS_SCENARIO_1);

        OrderItem packageItem = order.getItems().stream()
                .filter(i -> i.getItemType() == ItemType.PACKAGE)
                .findFirst()
                .orElseThrow();

        assertThat(packageItem.getChildren()).hasSize(EXPECTED_CHILDREN_IN_PACKAGE);
        assertThat(packageItem.getChildren().getFirst().getItemType()).isEqualTo(ItemType.PRODUCT);
    }

    @Test
    @DisplayName("Demonstrate expressive domain methods (Business shorthand)")
    void shouldCreateOrderUsingSemanticMethods() {
        // GIVEN: Using high-level business methods instead of technical setters
        Order orderToSave = OrderMother.standardOrder()
                .withBusinessCustomer()
                .withShippedOrder()
                .build();

        // WHEN
        Order saved = orderRepository.save(orderToSave);

        entityManager.flush();
        entityManager.clear();

        // THEN
        Order reloaded = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getCustomer().getFirstName()).isEqualTo("Marie");
        assertThat(reloaded.getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("Demonstrate 'without' usage (testing optionality)")
    void shouldCreateOrderUsingWithoutMethods() {
        // GIVEN: Explicitly removing parts of the aggregate to test nullable scenarios
        Order orderToSave = OrderMother.standardOrder()
                .withoutPaymentReference()
                .withoutItems()
                .build();

        // WHEN
        Order saved = orderRepository.save(orderToSave);

        entityManager.flush();
        entityManager.clear();

        // THEN
        Order reloaded = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getPaymentReference()).isNull();
        assertThat(reloaded.getItems()).isEmpty();
    }
}