--liquibase formatted sql

--changeset magdalena:insert-sample-brew-logs

-- Earl Grey - 8 times in last 30 days
INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '2 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '5 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 12, now() - interval '8 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '12 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '15 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 11, now() - interval '20 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '25 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '28 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Earl Grey';

-- Espresso - 12 times in last 30 days (most popular)
INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '1 day', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '3 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 7, now() - interval '4 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '6 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '9 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '11 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 9, now() - interval '13 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '16 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '18 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '22 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '26 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 8, now() - interval '29 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Espresso';

-- Sencha - 5 times in last 30 days
INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '3 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Sencha';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 9, now() - interval '7 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Sencha';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '14 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Sencha';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 10, now() - interval '21 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Sencha';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 11, now() - interval '27 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Sencha';

-- Cappuccino - 6 times in last 30 days
INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 12, now() - interval '2 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 11, now() - interval '6 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 12, now() - interval '10 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 12, now() - interval '17 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 11, now() - interval '23 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

INSERT INTO brew_log (beverage_id, amount_used, brewed_at, user_id)
SELECT b.id, 12, now() - interval '30 days', '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID
FROM beverage b WHERE b.name = 'Cappuccino';

--rollback DELETE FROM brew_log WHERE user_id = '55556e0e-d5f0-41b1-b295-6f71b2aff23f'::UUID;
