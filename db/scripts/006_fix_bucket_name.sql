--liquibase formatted sql
--changeset magda:006_fix_bucket_name

-- Update any existing records that use 'beverage-icons' to 'beverage-icon'
UPDATE beverage
SET image_url = REPLACE(image_url, 'beverage-icons/', 'beverage-icon/')
WHERE image_url LIKE 'beverage-icons/%';

--rollback UPDATE beverage SET image_url = REPLACE(image_url, 'beverage-icon/', 'beverage-icons/') WHERE image_url LIKE 'beverage-icon/%';
