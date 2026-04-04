-- Insert admin user
-- Note: Replace 'keycloak-user-uuid' with actual Keycloak user ID after creating user in Keycloak

INSERT INTO users (id, keycloak_id, username, email, first_name, last_name, email_verified, enabled, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'keycloak-user-uuid-here', -- Replace with actual Keycloak user UUID
    'admin',
    'admin@brewbuddy.com',
    'Admin',
    'User',
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- Insert ADMIN role for the admin user
INSERT INTO user_roles (id, user_id, role_name, created_at)
SELECT
    gen_random_uuid(),
    u.id,
    'ADMIN',
    CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'admin'
ON CONFLICT (user_id, role_name) DO NOTHING;

-- Also add USER role
INSERT INTO user_roles (id, user_id, role_name, created_at)
SELECT
    gen_random_uuid(),
    u.id,
    'USER',
    CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'admin'
ON CONFLICT (user_id, role_name) DO NOTHING;