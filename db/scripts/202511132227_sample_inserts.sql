--liquibase formatted sql
--changeset magda:202511132227_sample_inserts.sql

INSERT INTO beverage (type, name, brand)
SELECT 'TEA', 'Earl Grey', 'Twinings'
WHERE NOT EXISTS (SELECT 1 FROM beverage WHERE name = 'Earl Grey');

INSERT INTO beverage (type, name, brand)
SELECT 'TEA', 'Sencha', 'Ito En'
WHERE NOT EXISTS (SELECT 1 FROM beverage WHERE name = 'Sencha');

INSERT INTO beverage (type, name, brand)
SELECT 'COFFEE', 'Espresso', 'Lavazza'
WHERE NOT EXISTS (SELECT 1 FROM beverage WHERE name = 'Espresso');

INSERT INTO beverage (type, name, brand)
SELECT 'COFFEE', 'Cappuccino', 'Illy'
WHERE NOT EXISTS (SELECT 1 FROM beverage WHERE name = 'Cappuccino');


INSERT INTO beverage_quantity (beverage_id, quantity)
SELECT b.id, 250
FROM beverage b
WHERE b.name = 'Earl Grey'
  AND NOT EXISTS (SELECT 1 FROM beverage_quantity q WHERE q.beverage_id = b.id);

INSERT INTO beverage_quantity (beverage_id, quantity)
SELECT b.id, 200
FROM beverage b
WHERE b.name = 'Sencha'
  AND NOT EXISTS (SELECT 1 FROM beverage_quantity q WHERE q.beverage_id = b.id);

INSERT INTO beverage_quantity (beverage_id, quantity)
SELECT b.id, 500
FROM beverage b
WHERE b.name = 'Espresso'
  AND NOT EXISTS (SELECT 1 FROM beverage_quantity q WHERE q.beverage_id = b.id);

INSERT INTO beverage_quantity (beverage_id, quantity)
SELECT b.id, 300
FROM beverage b
WHERE b.name = 'Cappuccino'
  AND NOT EXISTS (SELECT 1 FROM beverage_quantity q WHERE q.beverage_id = b.id);

------------------------------------------------------------
-- TAGS
------------------------------------------------------------
INSERT INTO tag (name)
SELECT 'Strong'
WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name = 'Strong');

INSERT INTO tag (name)
SELECT 'Light'
WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name = 'Light');

INSERT INTO tag (name)
SELECT 'Aromatic'
WHERE NOT EXISTS (SELECT 1 FROM tag WHERE name = 'Aromatic');

------------------------------------------------------------
-- BEVERAGE_TAGS
------------------------------------------------------------
INSERT INTO beverage_tag (beverage_id, tag_id)
SELECT b.id, t.id
FROM beverage b
JOIN tag t ON t.name = 'Aromatic'
WHERE b.name IN ('Earl Grey', 'Sencha')
  AND NOT EXISTS (
      SELECT 1 FROM beverage_tag bt
      WHERE bt.beverage_id = b.id AND bt.tag_id = t.id
  );

INSERT INTO beverage_tag (beverage_id, tag_id)
SELECT b.id, t.id
FROM beverage b
JOIN tag t ON t.name = 'Strong'
WHERE b.name IN ('Espresso', 'Cappuccino')
  AND NOT EXISTS (
      SELECT 1 FROM beverage_tag bt
      WHERE bt.beverage_id = b.id AND bt.tag_id = t.id
  );

------------------------------------------------------------
-- BREW_LOGS
------------------------------------------------------------
INSERT INTO brew_log (beverage_id, amount_used, brewed_at)
SELECT b.id, 10, now()
FROM beverage b
WHERE b.name = 'Earl Grey'
  AND NOT EXISTS (
      SELECT 1 FROM brew_log bl
      WHERE bl.beverage_id = b.id
  );

INSERT INTO brew_log (beverage_id, amount_used, brewed_at)
SELECT b.id, 8, now()
FROM beverage b
WHERE b.name = 'Espresso'
  AND NOT EXISTS (
      SELECT 1 FROM brew_log bl
      WHERE bl.beverage_id = b.id
  );

--rollback DELETE FROM brew_log WHERE beverage_id IN (SELECT id FROM beverage WHERE name IN ('Earl Grey','Espresso'));
--rollback DELETE FROM beverage_tag WHERE beverage_id IN (SELECT id FROM beverage WHERE name IN ('Earl Grey','Sencha','Espresso','Cappuccino'));
--rollback DELETE FROM tag WHERE name IN ('Strong','Light','Aromatic');
--rollback DELETE FROM beverage_quantity WHERE beverage_id IN (SELECT id FROM beverage WHERE name IN ('Earl Grey','Sencha','Espresso','Cappuccino'));
--rollback DELETE FROM beverage WHERE name IN ('Earl Grey','Sencha','Espresso','Cappuccino');
