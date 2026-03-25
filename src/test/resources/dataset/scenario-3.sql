-- =============================================================================
-- SCÉNARIO 3 : Le piège de la FK oubliée
--
-- ⚠️  PIÈGE INTENTIONNEL :
--   L'item ci-dessous est inséré avec order_id = NULL.
--   Il existe bien en base, mais il est orphelin : aucun Order ne le
--   référence. La requête JPA (@OneToMany sur order_id) ne le renverra
--   jamais dans order.getItems().
--
--   En lisant le code Java du test, rien n'indique cette omission.
--   Le développeur s'attend à trouver 1 item — il en trouve 0.
--   C'est la déconnexion cognitive du paradigme SQL.
-- =============================================================================

-- 1. Customer
INSERT INTO customers (id, customer_type,
                       first_name, last_name, birth_date,
                       email, phone_number,
                       street, postal_code, city, state, country,
                       created_by, creation_date, update_date)
VALUES ('33333333-3333-3333-3333-333333333332',
        'INDIVIDUAL',
        'Jean', 'Dupont', '1985-06-15',
        'jean.dupont@example.com', '+33612345678',
        '1 rue de la Paix', '59000', 'Lille', 'Hauts-de-France', 'France',
        'sql-script', NOW(), NOW());

-- 2. PaymentReference
INSERT INTO payment_references (id, transaction_id, status, payment_processor,
                                created_by, creation_date, update_date)
VALUES ('33333333-3333-3333-3333-333333333333',
        'TXN-PENDING-SQL-003', 'PENDING', 'STRIPE',
        'sql-script', NOW(), NOW());

-- 3. Order
INSERT INTO orders (id, order_reference, status,
                    customer_id, payment_reference_id,
                    created_by, creation_date, update_date)
VALUES ('33333333-3333-3333-3333-333333333331',
        'CMD-SQL-003', 'VALIDATED',
        '33333333-3333-3333-3333-333333333332',
        '33333333-3333-3333-3333-333333333333',
        'sql-script', NOW(), NOW());

-- 4. ⚠️  Item ORPHELIN — order_id intentionnellement omis (NULL)
--        Le lien entre cet item et la commande n'existe pas.
--        PostgreSQL accepte l'INSERT (FK nullable), aucune erreur n'est levée.
--        Seul un développeur ouvrant CE fichier peut découvrir le problème.
INSERT INTO order_items (id, item_type, sku, product_reference, quantity,
                         price_value, currency_code, currency_symbol, currency_name,
                         order_id, -- ← NULL : FK délibérément absente
                         parent_item_id,
                         created_by, creation_date, update_date)
VALUES ('33333333-3333-3333-3333-333333333334',
        'PRODUCT', 'SKU-SQL-ORPHAN', 'PROD-ORPHAN', 1,
        29.99, 'EUR', '€', 'Euro',
        NULL, -- ← l'item existe en base mais n'est rattaché à aucune Order
        NULL,
        'sql-script', NOW(), NOW());

