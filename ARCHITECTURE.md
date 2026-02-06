# Employee Management Backend - Architecture Study Guide

## Hexagonal Architecture (Ports and Adapters)

**Study order (outside-in):**

1. **Entry point** - `Main.java` — boots the app, registers H2 console
2. **Inbound ports** - `api.behavior` package — 5 small interfaces (`CreateEmployee`, `FindEmployee`, etc.) that define *what* the system can do, without saying *how*
3. **Inbound adapter** - `EmployeeController` — receives HTTP requests, translates them into port calls. This is where REST meets your domain
4. **DTO** - `EmployeeDTO` — the shape of data crossing the API boundary
5. **Application service** - `EmployeeService` — implements all 5 ports, orchestrates the business logic
6. **Domain entities** - `Employee` and `Note` — JPA entities with the data model
7. **Mapper** - `EmployeeMapper` — converts between DTO and entity
8. **Outbound adapter** - `EmployeeRepository` — Spring Data JPA interface for database access

## Request Flow

```
HTTP Request
  → EmployeeController (inbound adapter)
    → calls port interface (e.g. FindEmployee)
      → EmployeeService (implements the port)
        → EmployeeMapper (DTO ↔ Entity)
        → EmployeeRepository (database)
    → EmployeeDTO ← response
  → HTTP Response
```

## The Hexagonal Pattern

- `api` = everything *outside* — the ports (contracts) and adapters (controller, DTO)
- `internal` = everything *inside* — service logic, entities, persistence

The key insight: the controller never touches `Employee` (entity) directly. It only knows about `EmployeeDTO` and the port interfaces. The service never knows it's being called via HTTP.

## Test Study Order

1. `EmployeeMapperTest` — pure logic, no mocks, easiest to follow
2. `EmployeeServiceTest` — shows how mocking isolates the service layer
3. `EmployeeControllerTest` — shows how to test the web layer in isolation
4. `EmployeeApiContractTest` — verifies the API contract (URLs, content types, response shapes)
5. `EmployeeIntegrationTest` — full stack, everything wired together

## Tech Stack

- Spring Boot 2.7.18
- Java 17
- H2 in-memory database
- JUnit 4
- 65 tests total
