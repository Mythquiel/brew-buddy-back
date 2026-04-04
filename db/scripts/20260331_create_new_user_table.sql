  -- liquibase formatted sql

  -- changeset brew-buddy:user_table
  -- comment: Recreate users table without Keycloak

  -- Drop dependent table first
  DROP TABLE IF EXISTS user_roles CASCADE;

  -- Drop and recreate users table
  DROP TABLE IF EXISTS users CASCADE;

  CREATE TABLE users (
      id UUID PRIMARY KEY,
      username VARCHAR(100) UNIQUE NOT NULL,
      email VARCHAR(255) UNIQUE NOT NULL,
      password_hash VARCHAR(255) NOT NULL,
      first_name VARCHAR(100),
      last_name VARCHAR(100),
      email_verified BOOLEAN DEFAULT FALSE,
      enabled BOOLEAN DEFAULT TRUE,
      created_at TIMESTAMP,
      updated_at TIMESTAMP
  );

  CREATE TABLE user_roles (
      id UUID PRIMARY KEY,
      user_id UUID REFERENCES users(id) ON DELETE CASCADE,
      role_name VARCHAR(50) NOT NULL,
      created_at TIMESTAMP,
      UNIQUE(user_id, role_name)
  );

  -- rollback DROP TABLE user_roles;
  -- rollback DROP TABLE users;