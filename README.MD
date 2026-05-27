# AirAsia Low Fare Calendar

A Spring Boot application that aggregates flight fare information from multiple providers and presents it as a calendar view. The application fetches flight prices from multiple sources, finds the lowest fares, and provides currency conversion capabilities.

## Overview

The AirAsia Low Fare Calendar is a backend service that:
- Aggregates flight fare data from multiple airline providers (Provider A, B, and C)
- Calculates and displays the lowest available fares for each day
- Supports multi-currency conversion for fare prices
- Utilizes Redis caching for performance optimization
- Implements event-driven architecture using Google Cloud Pub/Sub
- Includes circuit breaker pattern for resilient provider communication

## Technology Stack

- **Java Version:** 21
- **Framework:** Spring Boot 4.0.0
- **Build Tool:** Maven 3.9+
- **Runtime:** Eclipse Temurin JRE 21
- **Cache:** Redis 7
- **Message Queue:** Google Cloud Pub/Sub (with local emulator)
- **Containerization:** Docker & Docker Compose

### Key Dependencies

- **Spring Boot Starters:**
  - `spring-boot-starter-actuator` - Application monitoring and health checks
  - `spring-boot-starter-data-redis` - Redis integration
  - `spring-boot-starter-validation` - Bean validation
  - `spring-boot-starter-webmvc` - REST API development
  - `spring-boot-starter-data-redis-test` - Redis testing utilities

- **Cloud & Messaging:**
  - `spring-cloud-gcp-starter-pubsub` (v5.11.0) - Google Cloud Pub/Sub integration
  - `spring-cloud-dependencies` (v2025.1.0)

- **Resilience & Fault Tolerance:**
  - `spring-cloud-starter-circuitbreaker-resilience4j` - Circuit breaker implementation

- **Utilities:**
  - `jackson-databind` - JSON processing
  - `lombok` - Code generation and boilerplate reduction

## Project Structure

```
lowfare-calendar/
├── src/main/
│   ├── java/com/airasia/lowfare/
│   │   ├── LowfareCalendarApplication.java      # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── CalendarController.java          # REST API endpoints
│   │   ├── service/
│   │   │   ├── CalendarService.java             # Calendar logic with currency conversion
│   │   │   ├── AggregationService.java          # Multi-provider fare aggregation
│   │   │   ├── CurrencyService.java             # Currency conversion service
│   │   │   └── EventStateService.java           # Event state management
│   │   ├── provider/
│   │   │   ├── ProviderA.java                   # Airline provider A integration
│   │   │   ├── ProviderB.java                   # Airline provider B integration
│   │   │   └── ProviderC.java                   # Airline provider C integration
│   │   ├── pubsub/
│   │   │   ├── PriceSoldOutConsumer.java        # Pub/Sub message consumer
│   │   │   └── PriceSoldOutPublisher.java       # Pub/Sub message publisher
│   │   ├── model/
│   │   │   └── FareRecord.java                  # Fare data model
│   │   ├── dto/
│   │   │   ├── CalendarResponse.java            # API response DTO
│   │   │   ├── DayFare.java                     # Day-wise fare DTO
│   │   │   └── SoldOutEvent.java                # Sold-out event DTO
│   │   ├── cache/                               # Redis caching logic
│   │   ├── config/                              # Spring configuration
│   │   └── exception/                           # Custom exceptions
│   └── resources/
│       ├── application.properties                # Application configuration
│       ├── static/                               # Mock provider JSON files
│       │   ├── provider-a.json
│       │   ├── provider-b.json
│       │   └── provider-c.json
│       └── templates/
├── src/test/java/                               # Unit and integration tests
├── Dockerfile                                    # Multi-stage Docker build
├── docker-compose.yml                           # Container orchestration
├── run.sh                                        # Build and run script
├── pom.xml                                       # Maven configuration
└── mvnw, mvnw.cmd                               # Maven wrapper scripts
```

## API Endpoints

### Get Flight Calendar
**Endpoint:** `GET /api/v1/flights/calendar`

**Parameters:**
- `origin` (required) - Flight departure city code (e.g., DEL)
- `destination` (required) - Flight arrival city code (e.g., BKK)
- `month` (required) - Month for fare calendar (e.g., 2025-01)
- `currency` (required) - Currency code for fare display (e.g., USD, INR)

**Response:**
```json
{
  "origin": "DEL",
  "destination": "BKK",
  "currency": "USD",
  "fares": [
    {
      "date": "2025-01-01",
      "price": 145.50
    },
    {
      "date": "2025-01-02",
      "price": 152.75
    }
  ]
}
```

### Simulate Price Sold-Out Events
**Endpoint:** `GET /api/v1/flights/simulate`

Simulates price sold-out events via Pub/Sub for testing purposes.

### Publish Events
**Endpoint:** `GET /api/v1/flights/publish`

Publishes events to the Pub/Sub topic.

### Health Check
**Endpoint:** `GET /actuator/health`

Returns application health status including Redis connection status.

## Configuration

### Application Properties (`application.properties`)

```properties
spring.application.name=lowfare-calendar
management.endpoint.health.show-details=always

# Redis Configuration
spring.data.redis.host=${SPRING_DATA_REDIS_HOST:localhost}
spring.data.redis.port=${SPRING_DATA_REDIS_PORT:6379}
management.health.redis.enabled=false

# Google Cloud Pub/Sub Configuration
spring.cloud.gcp.pubsub.enabled=true
spring.cloud.gcp.project-id=local
spring.cloud.gcp.pubsub.emulator-host=${PUBSUB_EMULATOR_HOST:localhost:8085}

# Circuit Breaker Configuration (Provider A)
resilience4j.circuitbreaker.instances.providerA.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.providerA.wait-duration-in-open-state=5s
resilience4j.circuitbreaker.instances.providerA.sliding-window-size=5
```

## Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker & Docker Compose** (for containerized deployment)
- **Redis 7** (if running locally without Docker)
- **Google Cloud Pub/Sub Emulator** (for local testing)

## Installation & Setup

### Docker Deployment

1. **Build and start all services:**
   ```bash
   sh ./run.sh
   ```

   This script:
   - Stops existing containers
   - Cleans the target directory
   - Builds the Maven project
   - Builds Docker images
   - Starts all services via Docker Compose

2. **Application will be available at:** `http://localhost:8080`

3. **Access Redis:**
   ```bash
   docker exec -it lowfare-redis redis-cli
   ```

4. **Stop all services:**
   ```bash
   docker compose down
   ```

## Building the Application

### Maven Build
```bash
# Clean build
./mvnw clean package

# Build without running tests
./mvnw clean package -DskipTests

# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=CalendarServiceTest
```

### Docker Build
The `Dockerfile` uses a multi-stage build:
- **Stage 1:** Builds the application using Maven 3.9 with Java 21
- **Stage 2:** Creates a lightweight runtime image using Eclipse Temurin JRE 21

```bash
# Build Docker image
docker build -t lowfare-calendar:latest .

# Run the container
docker run -p 8080:8080 lowfare-calendar:latest
```

## Running Tests

The project includes comprehensive unit tests for core services:

```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report

# Run specific test file
./mvnw test -Dtest=AggregationServiceTest
```

**Test Files:**
- `AggregationServiceTest.java` - Multi-provider aggregation logic
- `CalendarServiceCacheTest.java` - Cache behavior and calendar generation
- `CurrencyServiceTest.java` - Currency conversion functionality
- `PriceSoldOutConsumerTest.java` - Pub/Sub consumer event handling
- `LowfareCalendarApplicationTests.java` - Application context and integration tests

## Architecture

### Multi-Provider Aggregation
The `AggregationService` uses `CompletableFuture` for parallel fetching from three providers:
1. Fetches fares from Provider A, B, and C concurrently
2. Aggregates all results into a single collection
3. Applies `Math.min()` to find the lowest fare for each date
4. Returns sorted results by date

### Caching Strategy
- Redis caches frequently accessed calendar data
- Reduces database queries and provider API calls
- Configurable TTL for cache expiration

### Resilience
- Circuit breaker pattern via Resilience4j
- Prevents cascading failures when providers are down
- 50% failure rate threshold with 5-second wait duration

### Event-Driven Architecture
- Google Cloud Pub/Sub for asynchronous event processing
- Handles price sold-out events
- Decouples components for better scalability

## Monitoring & Health

### Actuator Endpoints
- `/actuator/health` - Application health and dependencies status
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties

### Health Indicators
- Redis connection status
- Database connectivity
- Application initialization status

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_DATA_REDIS_HOST` | localhost | Redis server hostname |
| `SPRING_DATA_REDIS_PORT` | 6379 | Redis server port |
| `PUBSUB_EMULATOR_HOST` | localhost:8085 | Pub/Sub emulator host:port |

## Mock Data

The application includes mock provider data in JSON format for local testing:
- `src/main/resources/static/provider-a.json`
- `src/main/resources/static/provider-b.json`
- `src/main/resources/static/provider-c.json`

These mock files are copied into the Docker image for development and testing.

## Common Tasks

cd  /lowfare-calender
sh  ./run.sh


## Troubleshooting

### Connection Issues
- Ensure Redis is running: `redis-cli ping`
- Verify Pub/Sub emulator is running on port 8085
- Check Docker network: `docker network inspect lowfare-calendar_default`

### Tests Failing
- Clear Redis cache: `redis-cli FLUSHALL`
- Verify mock data files exist in `src/main/resources/static/`
- Ensure all dependencies are updated: `./mvnw dependency:resolve`

### Build Failures
- Clear Maven cache: `rm -rf ~/.m2/repository`
- Update Maven wrapper: `./mvnw wrapper:wrapper`
- Check Java version: `java -version` (requires Java 21+)

## Performance Considerations

1. **Parallel Provider Calls:** Uses `CompletableFuture` for concurrent API calls
2. **Caching:** Redis caching reduces repeated queries
3. **Circuit Breaker:** Prevents unnecessary calls to failing providers
4. **Batch Processing:** Efficient fare aggregation using streams

## Future Enhancements

- Support for additional airline providers
- Advanced filtering and sorting options
- Historical fare tracking
- Price alert notifications
- Analytics and reporting dashboards
- Rate limiting and API authentication

## Contributing

1. Create a feature branch
2. Make changes and add tests
3. Run full test suite: `./mvnw clean test`
4. Ensure code coverage meets standards
5. Submit pull request

## License
This project is proprietary software for AirAsia. All rights reserved.


## Test Latency

Run load-test.js

cmd: `k6 run load-test.js`

---

**Last Updated:** May 2025  
**Version:** 0.0.1-SNAPSHOT  
**Java Version:** 21  
**Spring Boot Version:** 4.0.0
