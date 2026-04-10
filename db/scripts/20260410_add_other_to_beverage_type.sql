--liquibase formatted sql

--changeset magdalena:add-other-to-beverage-type
ALTER TYPE beverage_type ADD VALUE IF NOT EXISTS 'OTHER';

--rollback -- Cannot remove enum values in PostgreSQL
