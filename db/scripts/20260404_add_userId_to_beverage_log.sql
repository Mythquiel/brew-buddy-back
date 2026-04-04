-- liquibase formatted sql

-- changeset brew-buddy:add_user_id_to_brew_log
-- comment: Add user_id column to brew_log table to track which user created each brew log entry

ALTER TABLE brew_log
ADD COLUMN user_id UUID;

ALTER TABLE brew_log
ADD CONSTRAINT fk_brew_log_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_brew_log_user_id ON brew_log(user_id);

-- rollback DROP INDEX IF EXISTS idx_brew_log_user_id;
-- rollback ALTER TABLE brew_log DROP CONSTRAINT IF EXISTS fk_brew_log_user;
-- rollback ALTER TABLE brew_log DROP COLUMN IF EXISTS user_id;