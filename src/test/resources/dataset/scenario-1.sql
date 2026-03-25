-- =============================================================================
-- SCÉNARIO 1 : Hiérarchie complexe — Order VALIDATED + Customer INDIVIDUAL
--              + PaymentReference SUCCESS + 2 top-level items
--              dont un PACKAGE contenant 1 sous-item PRODUCT
--
-- ⚠️  PROBLÈME DU PARADIGME SQL :
--   Le code Java de test ne contient ni ces IDs, ni ces statuts, ni ces
--   relations. Un développeur lisant SqlOrderRepositoryTest.java n'a aucune
--   idée de ce qui est réellement inséré ici. Tout refactoring du modèle
--   (renommage de colonne, nouveau champ obligatoire, changement de type)
--   exige d'ouvrir et de modifier manuellement ce fichier.
-- =============================================================================

-- 1. Customer (INDIVIDUAL, Lille)
INSERT INTO customers (id, customer_type,
                       first_name, last_name, birth_date,
                       email, phone_number,
                       street, postal_code, city, state, country,
                       created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111112',
        'INDIVIDUAL',
        'Jean', 'Dupont', '1985-06-15',
        'jean.dupont@example.com', '+33612345678',
        '1 rue de la Paix', '59000', 'Lille', 'Hauts-de-France', 'France',
        'sql-script', NOW(), NOW());

-- 2. PaymentReference (SUCCESS via STRIPE)
INSERT INTO payment_references (id, transaction_id, status, payment_processor,
                                created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111113',
        'TXN-SUCCESS-SQL-001', 'SUCCESS', 'STRIPE',
        'sql-script', NOW(), NOW());

-- 3. Order (VALIDATED)
INSERT INTO orders (id, order_reference, status,
                    customer_id, payment_reference_id,
                    created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111111',
        'CMD-SQL-001', 'VALIDATED',
        '11111111-1111-1111-1111-111111111112',
        '11111111-1111-1111-1111-111111111113',
        'sql-script', NOW(), NOW());

-- 4a. Item top-level #1 : PACKAGE (order_id → order, parent_item_id NULL)
INSERT INTO order_items (id, item_type, sku, product_reference, quantity,
                         price_value, currency_code, currency_symbol, currency_name,
                         order_id, parent_item_id,
                         created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111114',
        'PACKAGE', 'PKG-SQL-001', 'PACKAGE-WORKSTATION', 1,
        249.99, 'EUR', '€', 'Euro',
        '11111111-1111-1111-1111-111111111111', NULL,
        'sql-script', NOW(), NOW());

-- 4b. Item top-level #2 : PRODUCT standalone (order_id → order, parent_item_id NULL)
INSERT INTO order_items (id, item_type, sku, product_reference, quantity,
                         price_value, currency_code, currency_symbol, currency_name,
                         order_id, parent_item_id,
                         created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111115',
        'PRODUCT', 'SKU-SQL-002', 'PROD-MONITOR', 1,
        349.99, 'EUR', '€', 'Euro',
        '11111111-1111-1111-1111-111111111111', NULL,
        'sql-script', NOW(), NOW());

-- 4c. Sous-item PRODUCT enfant du PACKAGE
--     order_id NULL  (l'item appartient au PACKAGE, pas directement à l'Order)
--     parent_item_id → PACKAGE
INSERT INTO order_items (id, item_type, sku, product_reference, quantity,
                         price_value, currency_code, currency_symbol, currency_name,
                         order_id, parent_item_id,
                         created_by, creation_date, update_date)
VALUES ('11111111-1111-1111-1111-111111111116',
        'PRODUCT', 'SKU-SQL-001', 'PROD-KEYBOARD', 1,
        49.99, 'EUR', '€', 'Euro',
        NULL, '11111111-1111-1111-1111-111111111114',
        'sql-script', NOW(), NOW());

