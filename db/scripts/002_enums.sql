--liquibase formatted sql
--changeset magda:002_enums.sql

CREATE TYPE beverage_type AS ENUM ('TEA', 'COFFEE');

--rollback DROP TYPE IF EXISTS beverage_type CASCADE;