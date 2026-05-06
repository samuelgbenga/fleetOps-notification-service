# notification-service

Stateless email notification microservice for FleetOps.
Consumes `NotificationRequestEvent` from Kafka and sends emails via Mailtrap SMTP.

**No database. No JPA. No persistence.**

## Setup

1. Create a free account at [mailtrap.io](https://mailtrap.io)
2. Go to **Email Testing → Inboxes → SMTP Settings**
3. Copy your `Username` and `Password`
4. Set them as environment variables (see below)

## Key Environment Variables

| Variable | Description |
|---|---|
| `KAFKA_BOOTSTRAP` | Kafka broker address (default: `localhost:9092`) |
| `MAIL_HOST` | Mailtrap SMTP host (default: `sandbox.smtp.mailtrap.io`) |
| `MAIL_PORT` | Mailtrap SMTP port (default: `2525`) |
| `MAIL_USERNAME` | Your Mailtrap SMTP username |
| `MAIL_PASSWORD` | Your Mailtrap SMTP password |
| `MAIL_FROM` | From address shown in emails (default: `noreply@fleetops.com`) |

## How to run

Start this service **after** `fleet-core-service` is up.
Use the root `docker-compose.yml` from the parent folder:

```bash
# From the root fleetops/ folder
docker-compose up --build
```

Or run standalone (requires Kafka already running):

```bash
./mvnw spring-boot:run
```

## Kafka Topic

| Topic | Direction | Purpose |
|---|---|---|
| `notification.request` | Consumed | Receives notification events from fleet-core-service |
