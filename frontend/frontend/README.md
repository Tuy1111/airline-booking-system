# Airline Booking Console

React + TypeScript frontend for the Airline Booking System.

## Architecture

- Every `/api/v1/**` request goes through the API Gateway (`localhost:8080` in development).
- Login, registration, token refresh, and logout are handled by Keycloak through `keycloak-js`.
- The browser never sends trusted identity headers; the Gateway derives them from the verified token.
- Public flight search works without authentication. Booking, payment, profile, and notification APIs require a Keycloak access token.

## Run

Start Keycloak, Eureka, the Gateway, and the backend services, then run:

```bash
npm install
npm run dev
```

Development defaults:

```dotenv
VITE_API_GATEWAY_URL=http://localhost:8080
VITE_KEYCLOAK_URL=http://localhost:8180
VITE_KEYCLOAK_REALM=airline-booking
VITE_KEYCLOAK_CLIENT_ID=airline-frontend
```

The imported demo account is `testuser` / `password`. Configure real credentials before exposing the system publicly.
