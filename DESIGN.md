# Low Level Design (LLD)

## Objective

Design a scalable Low Fare Calendar backend that:

- Aggregates fares from multiple providers
- Returns lowest fare per day
- Supports dynamic currency conversion
- Uses Redis caching
- Supports asynchronous price updates
- Prevents stale cache responses

---

# 1. API Contracts

---

## Fetch Calendar

Endpoint:

```http
GET /api/v1/flights/calendar
```

Request:

```http
/api/v1/flights/calendar?
origin=DEL&
destination=BKK&
month=2026-05&
currency=MYR
```

Request Parameters:

| Parameter | Type | Required |
|---|---|---|
| origin | String | Yes |
| destination | String | Yes |
| month | String | Yes |
| currency | String | Yes |

---

Response

```json
{
  "origin":"DEL",
  "destination":"BKK",
  "currency":"MYR",
  "days":[
    {
      "date":"2026-05-03",
      "lowestFare":794
    }
  ]
}
```

---

## Publish Sold Out Event

Endpoint:

```http
POST /api/v1/flights/simulate
```

Response:

```json
{
 "status":"published"
}
```

---

# 2. Data Schema

## DayFare

```java
class DayFare {

String date;

Integer lowestFare;

}
```

---

## CalendarResponse

```java
class CalendarResponse {

String origin;

String destination;

String currency;

List<DayFare> days;

}
```

---

## SoldOutEvent

```java
class SoldOutEvent {

String eventId;

String route;

Long version;

}
```

---

# 3. Redis Cache Design

Pattern:

```text
Cache Aside
```

Cache Key:

```text
calendar:{origin}:{destination}:{month}:{currency}
```

Example:

```text
calendar:DEL:BKK:2026-05:MYR
```

TTL:

```text
30 minutes
```

---

## Cache Key Decision

Cache key contains:

| Field | Reason |
|---------------|-------------|
| origin | Separate source airports |
| destination | Route isolation |
| month | Calendar granularity |
| currency | Different conversion outputs |

Example:

```text
DEL:BKK:USD

≠

DEL:BKK:MYR
```

Without currency:

```text
incorrect cache reuse
```

Without month:

```text
old month may overwrite
new month
```

---

# 4. Class Diagram

```text
CalendarController

|

CalendarService

| CacheService

| CurrencyService

|

AggregationService

|

FlightProvider

-- ProviderA

-- ProviderB

-- ProviderC

|

PriceSoldOutPublisher

|

PriceSoldOutListener

|

PriceSoldOutConsumer



EventStateService
```

---

# 5. Architecture

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


# 6. Class Responsibilities

## CalendarController

Responsibilities:

- Expose REST APIs
- Validate request

Methods:

```java
getCalendar()
simulate()
```

---

## CalendarService

Responsibilities:

- Business orchestration
- Cache lookup
- Currency conversion

Methods:

```java
getCalendar()
```

---

## AggregationService

Responsibilities:

- Execute providers in parallel
- Aggregate lowest fare

Methods:

```java
getLowestFares()
```

Implementation:

```java
CompletableFuture
```

---

## CacheService

Responsibilities:

- Read cache
- Write cache
- Evict cache

Methods:

```java
get()

put()

evict()
```

---

## CurrencyService

Responsibilities:

- Currency conversion

Methods:

```java
convert()
```

---

## ProviderA/B/C

Responsibilities:

- Read static provider JSON
- Return provider fares

Methods:

```java
getCalendar()
```

---

## PriceSoldOutConsumer

Responsibilities:

- Process event
- Validate ordering
- Trigger cache invalidation

Methods:

```java
consume()
```

---

# 7. Design Patterns

---

## Strategy Pattern

Used For:

```text
FlightProvider
```

Implementation:

```text
ProviderA
ProviderB
ProviderC
```

Benefit:

```text
Easy provider extension
```

---

## Scatter Gather

Used For:

```text
AggregationService
```

Implementation:

```java
CompletableFuture
```

---

## Cache Aside

Used For:

```text
Redis
```

---

## Event Driven Architecture

Used For:

```text
Price updates
```

Flow:

```text
Publisher

↓

Listener

↓

Consumer
```

---

# 8. Currency Strategy

Flow:

```text
Provider Fare

↓

USD

↓

CurrencyService

↓

MYR / THB
```

---

# 9. Event Flow

```text
Price Sold Out

↓

Publisher

↓

Listener

↓

Consumer

↓

Cache Eviction

↓

Next Request Refresh
```

---

# 10. Performance Decisions

Target:

```text
1000 TPS

P99 < 500ms
```

Optimization:

- Redis
- Async Providers
- Event Updates
- Parallel Aggregation

---

# 11. Testing

Covered:

- Currency conversion
- Cache miss
- Event updates
- Duplicate events
- Controller testing