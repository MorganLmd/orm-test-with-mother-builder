-- =============================================================================
-- SCÉNARIO 2 : Cache L1 / flush+clear — Order avec exactement 3 items
--
-- ⚠️  PROBLÈME DU PARADIGME SQL :
--   Le nombre d'items (3) est une convention implicite entre ce fichier
--   et le test Java. Si un mainteneur ajoute ou retire un item ici sans
--   mettre à jour l'assertion Java (ou vice-versa), le test brise
--   silencieusement sans aucun lien statique entre les deux.
-- =============================================================================

-- 1. Customer
INSERT INTO customers (id, customer_type,
                       first_name, last_name, birth_date,
                       email, phone_number,
                       street, postal_code, city, state, country,
                       created_by, creation_date, update_date)
VALUES ('22222222-2222-2222-2222-222222222222',
        'INDIVIDUAL',
        'Jean', 'Dupont', '1985-06-15',
        'jean.dupont@example.com', '+33612345678',
        '1 rue de la Paix', '59000', 'Lille', 'Hauts-de-France', 'France',
        'sql-script', NOW(), NOW());

-- 2. PaymentReference
INSERT INTO payment_references (id, transaction_id, status, payment_processor,
                                created_by, creation_date, update_date)
VALUES ('22222222-2222-2222-2222-222222222223',
        'TXN-PENDING-SQL-002', 'PENDING', 'STRIPE',
        'sql-script', NOW(), NOW());

-- 3. Order
INSERT INTO orders (id, order_reference, status,
                    customer_id, payment_reference_id,
                    created_by, creation_date, update_date)
VALUES ('22222222-2222-2222-2222-222222222221',
        'CMD-SQL-002', 'VALIDATED',
        '22222222-2222-2222-2222-222222222222',
        '22222222-2222-2222-2222-222222222223',
        'sql-script', NOW(), NOW());

-- 4. Exactement 3 items PRODUCT top-level
INSERT INTO order_items (id, item_type, sku, product_reference, quantity,
                         price_value, currency_code, currency_symbol, currency_name,
                         order_id, parent_item_id,
                         created_by, creation_date, update_date)
VALUES ('22222222-2222-2222-2222-222222222224',
        'PRODUCT', 'SKU-SQL-A', 'PROD-A', 1,
        29.99, 'EUR', '€', 'Euro',
        '22222222-2222-2222-2222-222222222221', NULL,
        'sql-script', NOW(), NOW()),

       ('22222222-2222-2222-2222-222222222225',
        'PRODUCT', 'SKU-SQL-B', 'PROD-B', 2,
        59.99, 'EUR', '€', 'Euro',
        '22222222-2222-2222-2222-222222222221', NULL,
        'sql-script', NOW(), NOW()),

       ('22222222-2222-2222-2222-222222222226',
        'PRODUCT', 'SKU-SQL-C', 'PROD-C', 3,
        99.99, 'EUR', '€', 'Euro',
        '22222222-2222-2222-2222-222222222221', NULL,
        'sql-script', NOW(), NOW());

