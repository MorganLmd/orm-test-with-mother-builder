package com.demo.motherbuilder;

import com.demo.motherbuilder.entity.Order;
import com.demo.motherbuilder.entity.OrderStatus;
import com.demo.motherbuilder.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║  PARADIGME : @Sql — Scripts SQL préchargés                               ║
 * ╠══════════════════════════════════════════════════════════════════════════╣
 * ║  PROBLÈME FONDAMENTAL : déconnexion cognitive totale.                    ║
 * ║                                                                          ║
 * ║  En lisant CE fichier Java, le développeur ne sait pas :                 ║
 * ║    • Quels IDs sont utilisés                                             ║
 * ║    • Quels statuts / types sont assignés aux entités                     ║
 * ║    • Quelles relations existent entre les objets                         ║
 * ║    • Si la structure correspond encore au modèle actuel                  ║
 * ║                                                                          ║
 * ║  Toute évolution du modèle (nouveau champ obligatoire, renommage de      ║
 * ║  colonne, changement d'enum) casse silencieusement les scripts SQL       ║
 * ║  sans aucune alerte du compilateur.                                      ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class SqlOrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Fragile links to external file IDs
    private static final UUID SCENARIO_1_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SCENARIO_2_ID = UUID.fromString("22222222-2222-2222-2222-222222222221");
    private static final UUID SCENARIO_3_ID = UUID.fromString("33333333-3333-3333-3333-333333333331");

    @Test
    @Sql(scripts = "/dataset/scenario-1.sql")
    @DisplayName("Scenario 1: Complex Hierarchy - High Cognitive Load")
    void shouldRetrieveComplexOrderFromOpaqueSql() {
        // GIVEN: Data is hidden in scenario-1.sql.
        // The developer doesn't know the status or item count without switching files.

        // WHEN
        Order order = orderRepository.findById(SCENARIO_1_ID).orElseThrow();

        // THEN: Magic values from the script
        assertThat(order.getOrderReference()).isEqualTo("CMD-SQL-001");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.VALIDATED);
        assertThat(order.getItems()).hasSize(2);
    }

    @Test
    @Sql(scripts = "/dataset/scenario-2.sql")
    @DisplayName("Scenario 2: Implicit Convention - Maintenance Burden")
    void shouldDependOnImplicitSqlConvention() {
        // GIVEN: Test implicitly expects exactly 3 items from scenario-2.sql.

        // WHEN
        Order order = orderRepository.findById(SCENARIO_2_ID).orElseThrow();

        // THEN: Fragile assertion. Adding an item to the SQL file will break this test.
        assertThat(order.getItems()).hasSize(3);
    }

    @Test
    @Sql(scripts = "/dataset/scenario-3.sql")
    @DisplayName("Scenario 3: Missing Foreign Key - Invisible technical debt")
    void shouldFailSilentlyWhenDataIsInconsistentInSql() {
        // GIVEN: scenario-3.sql intentionally omits the order_id on an item (orphaned data).
        entityManager.clear();

        // WHEN
        Order order = orderRepository.findById(SCENARIO_3_ID).orElseThrow();

        // THEN: The code looks correct, but it finds 0 items.
        // This takes a long time to debug as the bug is in the opaque dataset, not the code.
        assertThat(order.getItems()).isEmpty();
    }
}

