-- liquibase formatted sql

-- changeset brew-buddy:add_uuid_default_to_users
-- comment: Add default UUID generation to users table id column

ALTER TABLE users
ALTER COLUMN id SET DEFAULT uuid_generate_v4();

-- rollback ALTER TABLE users ALTER COLUMN id DROP DEFAULT;
