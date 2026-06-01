# Local Infra Stack

```bash
# Start everything
docker compose -f infra/docker/docker-compose.yml up -d

# Tail logs
docker compose -f infra/docker/docker-compose.yml logs -f
```

## Endpoints

| Service     | URL                          | Note                                 |
| ----------- | ---------------------------- | ------------------------------------ |
| Postgres    | localhost:5432               | user/pw: `abs` / `abs123`            |
| RabbitMQ    | http://localhost:15672       | guest / guest                        |
| Kafka       | localhost:9092               | bootstrap                            |
| Redis       | localhost:6379               |                                      |
| MailHog UI  | http://localhost:8025        | catches every email sent via 1025    |
| Zipkin      | http://localhost:9411        | distributed traces                   |
| Prometheus  | http://localhost:9090        | scrapes ports 8080–8085 on host      |
| Grafana     | http://localhost:3000        | admin / admin · auto-provisioned     |

## Per-service databases

On first start, `postgres/init-multi-db.sh` creates: `flight_db`, `booking_db`,
`user_db`, `payment_db`, `notify_db`.

If you already have a `pgdata` volume, drop it once to re-run the init script:
`docker compose -f infra/docker/docker-compose.yml down -v`.

## Scraping note

Prometheus runs in Docker but scrapes services running on **your host** at
`host.docker.internal:80xx`. If you containerise the services, change
`prometheus/prometheus.yml` to use the service name and the bridge network.

## Notification queues / topics

The notification-service listens to:

- RabbitMQ queues: `cmd.email.send`, `cmd.sms.send` (JSON, see `SendEmailCommand`)
- Kafka topics: `booking.confirmed`, `booking.cancelled`, `payment.failed`

Smoke test (after services are up):

```bash
# Direct REST send (bypass broker)
curl -X POST http://localhost:8085/api/v1/notifications/test-send \
  -H "Content-Type: application/json" \
  -d '{
    "templateCode": "BOOKING_CONFIRMED",
    "locale": "vi",
    "userId": 1,
    "recipient": "demo@example.com",
    "variables": {
      "passengerName": "Nguyen Van A",
      "bookingCode": "ABS-001",
      "flightNo": "VN123",
      "from": "HAN",
      "to": "SGN",
      "departureTime": "2026-06-30 09:00",
      "seatNo": "12A",
      "amount": 2500000,
      "currency": "VND"
    }
  }'

# Open MailHog at http://localhost:8025 to see the email
```
