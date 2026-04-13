# Brew Buddy Backend

A Spring Boot REST API for managing beverages, tracking brew logs, and inventory management.

## Features

- 🍵 **Beverage Management** - CRUD operations for beverages (tea, coffee, etc.) with admin-only write access
- 📊 **Brew Log Tracking** - Record and track your brewing history with user attribution (who brewed what, when)
- 📦 **Inventory Management** - Track beverage quantities for authenticated users
- 🏷️ **Tag System** - Organize beverages with custom tags
- 🔐 **JWT Authentication** - Secure API with token-based auth and role-based access control
- 👥 **User Management** - User accounts with auto-generated UUIDs and role assignments
- 🖼️ **Image Storage** - Supabase integration for beverage images with signed URLs
- 📄 **Database Migrations** - Liquibase for version-controlled schema changes

## Tech Stack

- **Java 17+**
- **Spring Boot 3.5.7**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Spring Actuator
- **PostgreSQL** - Primary database
- **Liquibase** - Database migrations
- **MapStruct** - Object mapping
- **Lombok** - Boilerplate reduction
- **JWT (JJWT)** - Token authentication
- **Supabase** - Image storage
- **JUnit 5 & Mockito** - Testing

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 12+
- Gradle 8.x

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd brew-buddy-back
   ```

2. **Set up the database**
   ```bash
   # Create database
   createdb brewbuddy
   
   # Run migrations
   ./gradlew liquibaseUpdate
   ```

3. **Run the application**
   ```bash
   # Using Gradle
   ./gradlew bootRun --args='--spring.profiles.active=local'
   
   # Or build and run JAR
   ./gradlew build
   java -jar build/libs/brew-buddy-back-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
   ```

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - Health check: `http://localhost:8080/actuator/health`

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests BeverageServiceTest
```

## API Endpoints

The API contract is documented in [`openapi.yaml`](./openapi.yaml). It includes endpoint paths, request and response schemas, pagination parameters, file upload details, and JWT authentication requirements.

To inspect it locally, open the file in an OpenAPI-compatible viewer such as Swagger Editor, Redoc, or your IDE's OpenAPI plugin.

## Database Schema

### Main Tables

- **`beverage`** - Core beverage information (type, name, brand, brew times, image)
- **`beverage_quantity`** - Inventory tracking (one-to-one with beverage)
- **`brew_log`** - Brewing history with timestamps and user attribution
- **`tag`** - Custom tags for categorization
- **`beverage_tag`** - Many-to-many relationship (beverages ↔ tags)
- **`users`** - User accounts (id auto-generated, username, email, roles)
- **`user_roles`** - User role assignments (ADMIN, USER, etc.)

### Key Relationships

```
beverage (1) ─── (1) beverage_quantity
beverage (1) ─── (N) brew_log
beverage (N) ─── (N) tag (via beverage_tag)
users (1) ─── (N) brew_log (tracks who brewed what)
users (1) ─── (N) user_roles
```

## Database Migrations

Using Liquibase for schema versioning:

```bash
# Check migration status
./gradlew liquibaseStatus

# Apply pending migrations
./gradlew liquibaseUpdate

# Preview SQL (without applying)
./gradlew liquibaseUpdateSQL

# Rollback to specific tag
./gradlew liquibaseRollback -Ptag=v1.0
```

### Migration Scripts

All migration scripts are in `/db/scripts/`:

## Configuration

### Profiles

- **local** - Local development with verbose logging, SQL debugging, all actuator endpoints
- **prod** - Production with minimal logging (WARN level), restricted actuator endpoints (health only)

### Application Properties Structure

The properties are structured to eliminate duplication:

- **`application.properties`** - Common settings shared across all environments (database config, JPA settings) + required environment variables (no defaults)
- **`application-local.properties`** - Local dev overrides only (verbose logging, SQL formatting, more actuator endpoints) - **Not in git, contains secrets**
- **`application-prod.properties`** - Production overrides only (restrictive logging, minimal actuator endpoints)

### JWT Configuration

JWT tokens are validated using the `AUTH_JWT_SECRET`. The token must contain:
- `sub` - User ID (UUID)
- `roles` - List of roles (e.g., `["ROLE_USER", "ROLE_ADMIN"]`)

Example JWT payload:
```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "roles": ["ROLE_USER"],
  "iat": 1712188800,
  "exp": 1712275200
}
```

## Security

### Authentication & Authorization

- **JWT-based authentication** - Token validation on protected endpoints
- **Role-based access control (RBAC)**:
  - **Public (no auth)**: Read beverages and tags (GET `/api/v1/beverages/**`, `/api/v1/tags/**`)
  - **Authenticated users**: Manage brew logs, beverage quantities
  - **ADMIN role**: Create/update/delete beverages and tags
- **CORS** - Configurable allowed origins
- **Stateless sessions** - No server-side session storage
- **Security headers** - CSRF disabled for stateless API

### Access Control Matrix

| Endpoint | Anonymous | Authenticated | Admin |
|----------|-----------|---------------|-------|
| GET /api/v1/beverages | ✅ | ✅ | ✅ |
| POST/PATCH/DELETE /api/v1/beverages | ❌ | ❌ | ✅ |
| GET /api/v1/tags | ✅ | ✅ | ✅ |
| POST/PATCH/DELETE /api/v1/tags | ❌ | ❌ | ✅ |
| /api/v1/brewLog | ❌ | ✅ | ✅ |
| /api/v1/beverageQuantity | ❌ | ✅ | ✅ |
| /actuator/health | ✅ | ✅ | ✅ |

## Monitoring

Spring Boot Actuator endpoints:

- `/actuator/health` - Application health status
- `/actuator/info` - Application information (local only)
- `/actuator/metrics` - Metrics (local only)

### Building for Production

```bash
# Build JAR
./gradlew clean build

# Run with production profile
java -jar build/libs/brew-buddy-back-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod

# With custom JVM options
java -Xms512m -Xmx1024m \
  -XX:+UseG1GC \
  -jar build/libs/brew-buddy-back-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

## Troubleshooting

### Common Issues

**Connection refused to database**
- Check DB_URL, DB_USERNAME, DB_PASSWORD
- Ensure PostgreSQL is running
- Verify network connectivity

**JWT authentication fails**
- Verify AUTH_JWT_SECRET is set correctly (min 256 bits)
- Check token expiration
- Ensure AUTH_SERVICE_URL is accessible

**Liquibase fails to run**
- Check database connection
- Verify migration scripts syntax
- Review liquibase.properties configuration

**Tests fail with security errors**
- Ensure `@WithMockUser` is used in tests
- Check TestSecurityConfig is imported
- Verify CSRF token in POST/PATCH/DELETE tests

## Contact

````
Magda Świtała
ma.switala@gmail.com
````
