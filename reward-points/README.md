# Customer Reward Points

Spring Boot REST API for calculating customer reward points per month and total.

## Reward Rules

- Up to $50: 0 points
- $50 to $100: 1 point for each dollar above $50
- Above $100: 50 points + 2 points for each dollar above $100

Example: $120 = 50 + (20 x 2) = 90 points.

## Technology

- Java 8
- Spring Boot 2.7.18
- Spring Data JPA
- H2
- Maven
- JUnit 5
- Mockito
- MockMvc

## API

GET `/api/rewards?startDate=2026-01-01&endDate=2026-03-31`

GET `/api/rewards/{customerId}?startDate=2026-01-01&endDate=2026-03-31`

Months are not hardcoded. The requested date range is supplied to the API and transactions are grouped dynamically by `yyyy-MM`.

## Structure

- controller: REST endpoints
- service: business logic
- repository: database access
- entity: JPA entities
- dto: API response models
- exception: exception handling
- resources: SQL scripts and configuration
- test: unit and integration tests

## Database

Sample data is provided through `schema.sql` and `data.sql`. No sample data is hardcoded in Java.

## Run

```bash
mvn clean test
mvn spring-boot:run
```

Application URL: `http://localhost:8080`

## H2 Console

URL: `http://localhost:8080/h2-console`

JDBC URL: `jdbc:h2:mem:rewarddb`
Username: `sa`
Password: blank

## GitHub

Do not commit `target/`, `bin/`, IDE files, or archive files.
