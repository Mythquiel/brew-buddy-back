--liquibase formatted sql
--changeset magdalena:20260413_insert_beverages_from_csv

CREATE TEMP TABLE tmp_beverage_import (
    brand TEXT NOT NULL,
    name TEXT NOT NULL,
    tags TEXT NOT NULL,
    brew_time_min_sec SMALLINT NOT NULL,
    brew_time_max_sec SMALLINT NOT NULL,
    type beverage_type NOT NULL
) ON COMMIT DROP;

INSERT INTO tmp_beverage_import (brand, name, tags, brew_time_min_sec, brew_time_max_sec, type)
VALUES
    ('Five o''clock', 'Sencha imbir cytryna', 'ZIELONA;SENCHA;IMBIR;CYTRYNA;ORZEŹWIAJĄCA', 120, 180, 'TEA'),
    ('Lipton', 'Zielona Earl Grey', 'ZIELONA;EARL GREY;BERGAMOTKA;CYTRUSOWA', 120, 240, 'TEA'),
    ('Duka', 'Herbata biała jagoda, morwa, bławatek', 'BIAŁA;JAGODA;MORWA;BŁAWATEK;OWOCOWA;KWIATOWA', 120, 180, 'TEA'),
    ('Zadiko', 'Mission chill', 'RUMIANEK;MIĘTA;ZIOŁOWA;RELAKSUJĄCA;BEZKOFEINOWA', 300, 420, 'TEA'),
    ('Lipton', 'Poziomka z mandarynką', 'OWOCOWA;POZIOMKA;MANDARYNKA;CYTRUSOWA;BEZKOFEINOWA', 300, 600, 'TEA'),
    ('Remsey', 'Cold brew czarna porzeczka z tymiankiem', 'COLD BREW;CZARNA PORZECZKA;TYMIANEK;OWOCOWA;ZIOŁOWA;NA ZIMNO', 300, 480, 'TEA'),
    ('Five o''clock', 'Piernikowa', 'ZIMOWA;PIERNIKOWA;KORZENNA;AROMATYZOWANA', 300, 420, 'TEA'),
    ('Lipton', 'Zielona cytrusowa', 'ZIELONA;CYTRUSOWA;ORZEŹWIAJĄCA', 120, 240, 'TEA'),
    ('Duka', 'Cejlońska limonka malina', 'CEJLOŃSKA;CEYLON;CZARNA;LIMONKA;MALINA;OWOCOWA', 180, 300, 'TEA'),
    ('Zadiko', 'Breakfast tea', 'CZARNA;BREAKFAST;KLASYCZNA;MOCNA', 180, 300, 'TEA'),
    ('Tetley', 'Earl Grey', 'CZARNA;EARL GREY;BERGAMOTKA;KLASYCZNA', 180, 300, 'TEA'),
    ('Auchan', 'Biała róża bławatek', 'BIAŁA;RÓŻA;BŁAWATEK;KWIATOWA;DELIKATNA', 120, 180, 'TEA'),
    ('Dahzali', 'Pure Ceylon Tea', 'CEYLON;CZARNA;KLASYCZNA;MOCNA', 180, 300, 'TEA'),
    ('Whittard of Chelsea', 'English Rose', 'CZARNA;RÓŻA;KWIATOWA;AROMATYZOWANA;SŁODKA', 180, 300, 'TEA'),
    ('Whittard of Chelsea', 'Covent Garden Blend', 'CZARNA;BLEND;AROMATYZOWANA;KLASYCZNA', 180, 300, 'TEA'),
    ('Whittard of Chelsea', 'Golden Camomile', 'RUMIANEK;ZIOŁOWA;KWIATOWA;RELAKSUJĄCA;BEZKOFEINOWA', 300, 420, 'TEA'),
    ('Whittard of Chelsea', 'Mango & Bergamot', 'OWOCOWA;MANGO;BERGAMOTKA;CYTRUSOWA;AROMATYZOWANA', 300, 600, 'TEA'),
    ('Whittard of Chelsea', 'Peppermint', 'MIĘTA;ZIOŁOWA;ORZEŹWIAJĄCA;BEZKOFEINOWA', 300, 300, 'TEA'),
    ('Herbapol', 'Mięta mango na zimno', 'MIĘTA;MANGO;OWOCOWA;ZIOŁOWA;NA ZIMNO;BEZKOFEINOWA', 300, 480, 'TEA'),
    ('Café d''Or', 'Kawa rozpuszczalna', 'KAWA;ROZPUSZCZALNA;KLASYCZNA', 30, 60, 'COFFEE'),
    ('Starbucks', 'Caffe latte', 'KAPSUŁKOWA;LATTE;MLECZNA;KAWA', 60, 120, 'COFFEE'),
    ('Starbucks', 'Pumpkin Spice latte', 'KAPSUŁKOWA;LATTE;PUMPKIN SPICE;KORZENNA;MLECZNA', 60, 120, 'COFFEE'),
    ('Starbucks', 'Caramel Macchiato', 'KAPSUŁKOWA;MACCHIATO;KARMEL;MLECZNA', 60, 120, 'COFFEE'),
    ('Nescafé', 'Cappuccino', 'KAPSUŁKOWA;CAPPUCCINO;MLECZNA;PIANKA', 60, 120, 'COFFEE'),
    ('Nescafé', 'Americano', 'KAPSUŁKOWA;AMERICANO;CZARNA KAWA', 30, 60, 'COFFEE');

INSERT INTO beverage (type, name, brand, brew_time_min_sec, brew_time_max_sec)
SELECT i.type, i.name, i.brand, i.brew_time_min_sec, i.brew_time_max_sec
FROM tmp_beverage_import i
WHERE NOT EXISTS (
    SELECT 1
    FROM beverage b
    WHERE lower(b.name) = lower(i.name)
      AND lower(coalesce(b.brand, '')) = lower(i.brand)
);

INSERT INTO tag (name)
SELECT DISTINCT trim(tag_name)
FROM tmp_beverage_import i
CROSS JOIN LATERAL regexp_split_to_table(i.tags, ';') AS tag_name
WHERE trim(tag_name) <> ''
  AND NOT EXISTS (
      SELECT 1
      FROM tag t
      WHERE lower(t.name) = lower(trim(tag_name))
  );

INSERT INTO beverage_tag (beverage_id, tag_id)
SELECT b.id, t.id
FROM tmp_beverage_import i
JOIN beverage b
  ON lower(b.name) = lower(i.name)
 AND lower(coalesce(b.brand, '')) = lower(i.brand)
CROSS JOIN LATERAL regexp_split_to_table(i.tags, ';') AS tag_name
JOIN tag t ON lower(t.name) = lower(trim(tag_name))
WHERE trim(tag_name) <> ''
  AND NOT EXISTS (
      SELECT 1
      FROM beverage_tag bt
      WHERE bt.beverage_id = b.id
        AND bt.tag_id = t.id
  );

--rollback DELETE FROM beverage_tag WHERE beverage_id IN (SELECT id FROM beverage WHERE (brand, name) IN (('Five o''clock', 'Sencha imbir cytryna'), ('Lipton', 'Zielona Earl Grey'), ('Duka', 'Herbata biała jagoda, morwa, bławatek'), ('Zadiko', 'Mission chill'), ('Lipton', 'Poziomka z mandarynką'), ('Remsey', 'Cold brew czarna porzeczka z tymiankiem'), ('Five o''clock', 'Piernikowa'), ('Lipton', 'Zielona cytrusowa'), ('Duka', 'Cejlońska limonka malina'), ('Zadiko', 'Breakfast tea'), ('Tetley', 'Earl Grey'), ('Auchan', 'Biała róża bławatek'), ('Dahzali', 'Pure Ceylon Tea'), ('Whittard of Chelsea', 'English Rose'), ('Whittard of Chelsea', 'Covent Garden Blend'), ('Whittard of Chelsea', 'Golden Camomile'), ('Whittard of Chelsea', 'Mango & Bergamot'), ('Whittard of Chelsea', 'Peppermint'), ('Herbapol', 'Mięta mango na zimno'), ('Café d''Or', 'Kawa rozpuszczalna'), ('Starbucks', 'Caffe latte'), ('Starbucks', 'Pumpkin Spice latte'), ('Starbucks', 'Caramel Macchiato'), ('Nescafé', 'Cappuccino'), ('Nescafé', 'Americano')));
--rollback DELETE FROM beverage_quantity WHERE beverage_id IN (SELECT id FROM beverage WHERE (brand, name) IN (('Five o''clock', 'Sencha imbir cytryna'), ('Lipton', 'Zielona Earl Grey'), ('Duka', 'Herbata biała jagoda, morwa, bławatek'), ('Zadiko', 'Mission chill'), ('Lipton', 'Poziomka z mandarynką'), ('Remsey', 'Cold brew czarna porzeczka z tymiankiem'), ('Five o''clock', 'Piernikowa'), ('Lipton', 'Zielona cytrusowa'), ('Duka', 'Cejlońska limonka malina'), ('Zadiko', 'Breakfast tea'), ('Tetley', 'Earl Grey'), ('Auchan', 'Biała róża bławatek'), ('Dahzali', 'Pure Ceylon Tea'), ('Whittard of Chelsea', 'English Rose'), ('Whittard of Chelsea', 'Covent Garden Blend'), ('Whittard of Chelsea', 'Golden Camomile'), ('Whittard of Chelsea', 'Mango & Bergamot'), ('Whittard of Chelsea', 'Peppermint'), ('Herbapol', 'Mięta mango na zimno'), ('Café d''Or', 'Kawa rozpuszczalna'), ('Starbucks', 'Caffe latte'), ('Starbucks', 'Pumpkin Spice latte'), ('Starbucks', 'Caramel Macchiato'), ('Nescafé', 'Cappuccino'), ('Nescafé', 'Americano')));
--rollback DELETE FROM beverage WHERE (brand, name) IN (('Five o''clock', 'Sencha imbir cytryna'), ('Lipton', 'Zielona Earl Grey'), ('Duka', 'Herbata biała jagoda, morwa, bławatek'), ('Zadiko', 'Mission chill'), ('Lipton', 'Poziomka z mandarynką'), ('Remsey', 'Cold brew czarna porzeczka z tymiankiem'), ('Five o''clock', 'Piernikowa'), ('Lipton', 'Zielona cytrusowa'), ('Duka', 'Cejlońska limonka malina'), ('Zadiko', 'Breakfast tea'), ('Tetley', 'Earl Grey'), ('Auchan', 'Biała róża bławatek'), ('Dahzali', 'Pure Ceylon Tea'), ('Whittard of Chelsea', 'English Rose'), ('Whittard of Chelsea', 'Covent Garden Blend'), ('Whittard of Chelsea', 'Golden Camomile'), ('Whittard of Chelsea', 'Mango & Bergamot'), ('Whittard of Chelsea', 'Peppermint'), ('Herbapol', 'Mięta mango na zimno'), ('Café d''Or', 'Kawa rozpuszczalna'), ('Starbucks', 'Caffe latte'), ('Starbucks', 'Pumpkin Spice latte'), ('Starbucks', 'Caramel Macchiato'), ('Nescafé', 'Cappuccino'), ('Nescafé', 'Americano'));
--rollback DELETE FROM tag WHERE name IN ('ZIELONA', 'SENCHA', 'IMBIR', 'CYTRYNA', 'ORZEŹWIAJĄCA', 'EARL GREY', 'BERGAMOTKA', 'CYTRUSOWA', 'BIAŁA', 'JAGODA', 'MORWA', 'BŁAWATEK', 'OWOCOWA', 'KWIATOWA', 'RUMIANEK', 'MIĘTA', 'ZIOŁOWA', 'RELAKSUJĄCA', 'BEZKOFEINOWA', 'COLD BREW', 'CZARNA PORZECZKA', 'TYMIANEK', 'NA ZIMNO', 'ZIMOWA', 'PIERNIKOWA', 'KORZENNA', 'AROMATYZOWANA', 'CEJLOŃSKA', 'CEYLON', 'CZARNA', 'LIMONKA', 'MALINA', 'BREAKFAST', 'KLASYCZNA', 'MOCNA', 'RÓŻA', 'DELIKATNA', 'SŁODKA', 'BLEND', 'MANGO', 'KAWA', 'ROZPUSZCZALNA', 'KAPSUŁKOWA', 'LATTE', 'MLECZNA', 'PUMPKIN SPICE', 'MACCHIATO', 'KARMEL', 'CAPPUCCINO', 'PIANKA', 'AMERICANO', 'CZARNA KAWA') AND NOT EXISTS (SELECT 1 FROM beverage_tag bt WHERE bt.tag_id = tag.id);
