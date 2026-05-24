# Kafka Demo

A hands-on demonstration of event-driven communication between two Spring Boot microservices using Apache Kafka.

## Architecture

```
┌─────────────────┐        orders-topic        ┌──────────────────────────┐
│  order-service  │  ──────────────────────►  │  notification-service    │
│  (Producer)     │        Kafka Broker         │  (Consumer)              │
│  :8080          │                             │  :8081                   │
└─────────────────┘                             └──────────────────────────┘
```

- **order-service** exposes a REST endpoint to place orders and publishes an `OrderEvent` to the `orders-topic` Kafka topic.
- **notification-service** listens on that topic (consumer group `notification-group`) and logs/processes each incoming event.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Kafka
- Apache Kafka (KRaft mode — no ZooKeeper)
- Kafka UI
- Testcontainers (integration tests)
- Lombok

## Project Structure

```
kafka-demo/
├── docker-compose.yml       # Kafka broker + Kafka UI
├── order-service/           # REST producer (port 8080)
└── notification-service/    # Kafka consumer (port 8081)
```

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21
- Maven (or use the included `mvnw` wrapper)

### 1. Start the infrastructure

```bash
docker compose up -d
```

This starts:
- Kafka broker on `localhost:9094` (KRaft, 3 partitions, auto topic creation enabled)
- Kafka UI on `http://localhost:8090`

### 2. Start the services

In two separate terminals:

```bash
# Terminal 1
cd order-service && ./mvnw spring-boot:run

# Terminal 2
cd notification-service && ./mvnw spring-boot:run
```

### 3. Place an order

```http
POST http://localhost:8080/api/v1/orders
Content-Type: application/json

{
  "customerId": "client-armand-010",
  "productName": "MacBook Pro M3",
  "quantity": 1,
  "totalAmount": 2999.00
}
```

The order-service publishes an `OrderEvent` to `orders-topic`. The notification-service picks it up and logs the event details including topic, partition, offset, and order fields.

## Kafka Configuration

| Setting | Value |
|---|---|
| Bootstrap servers | `localhost:9094` |
| Topic | `orders-topic` |
| Partitions | 3 |
| Consumer group | `notification-group` |
| Listener concurrency | 3 |
| Producer acks | `all` (with 3 retries) |

## Running Tests

```bash
# From either service directory
./mvnw test
```

Tests use Testcontainers to spin up a real Kafka broker — no mocks.
