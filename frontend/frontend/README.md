# Airline Booking Console

React + TypeScript frontend for the Airline Booking System microservices.

## Architecture

- `src/shared/api`: common HTTP client, error parsing, page helpers.
- `src/shared/config`: service catalog and base URL configuration.
- `src/features/flights`: flight-search-service client and DTOs.
- `src/features/bookings`: booking-service client and DTOs.
- `src/features/users`: user-service client and DTOs.
- `src/features/payments`: payment-service client and DTOs.
- `src/features/notifications`: notification-service client and DTOs.

The UI intentionally calls the same REST paths as the backend (`/api/v1/...`). In development, Vite proxies each path to its owning service:

| Path | Service |
| --- | --- |
| `/api/v1/flights`, `/api/v1/airports`, `/api/v1/airlines`, `/api/v1/routes` | `localhost:8081` |
| `/api/v1/bookings` | `localhost:8082` |
| `/bookings` | `localhost:8082` fallback for the current booking controller |
| `/api/v1/users` | `localhost:8083` |
| `/api/v1/payments` | `localhost:8084` |
| `/api/v1/notifications` | `localhost:8085` |

For a VPS backend, set:

```bash
VITE_BACKEND_HOST=http://143.198.213.125
```

## Run

```bash
npm install
npm run dev
```

Optional production/gateway config:

```bash
VITE_API_BASE_URL=http://localhost:8080 npm run dev
```
