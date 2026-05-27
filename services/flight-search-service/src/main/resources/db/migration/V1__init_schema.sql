-- flight-search-service · initial schema
-- Tham chiếu docs/design.md §II.2

CREATE TABLE airport (
    iata_code   VARCHAR(3)   PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    city        VARCHAR(50)  NOT NULL,
    country     VARCHAR(50)  NOT NULL
);

CREATE TABLE airline (
    code        VARCHAR(2)   PRIMARY KEY,
    name        VARCHAR(100) NOT NULL
);

CREATE TABLE route (
    id              BIGSERIAL  PRIMARY KEY,
    from_airport    VARCHAR(3) NOT NULL REFERENCES airport(iata_code),
    to_airport      VARCHAR(3) NOT NULL REFERENCES airport(iata_code),
    distance_km     INT,
    CONSTRAINT uk_route UNIQUE (from_airport, to_airport)
);

CREATE TABLE flight (
    id              BIGSERIAL     PRIMARY KEY,
    flight_no       VARCHAR(10)   NOT NULL UNIQUE,
    route_id        BIGINT        NOT NULL REFERENCES route(id),
    airline_code    VARCHAR(2)    NOT NULL REFERENCES airline(code),
    departure_time  TIMESTAMP     NOT NULL,
    arrival_time    TIMESTAMP     NOT NULL,
    total_seats     INT           NOT NULL,
    base_price      DECIMAL(12,2) NOT NULL,
    aircraft_type   VARCHAR(20),
    status          VARCHAR(20)   NOT NULL DEFAULT 'SCHEDULED',
    version         BIGINT        NOT NULL DEFAULT 0,
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_flight_status CHECK (status IN ('SCHEDULED','DELAYED','CANCELLED','DEPARTED'))
);

CREATE INDEX idx_flight_search ON flight(route_id, departure_time);
CREATE INDEX idx_flight_status ON flight(status);

CREATE TABLE seat_inventory (
    flight_id   BIGINT  PRIMARY KEY REFERENCES flight(id) ON DELETE CASCADE,
    total       INT     NOT NULL,
    available   INT     NOT NULL,
    held        INT     NOT NULL DEFAULT 0,
    booked      INT     NOT NULL DEFAULT 0,
    version     BIGINT  NOT NULL DEFAULT 0,
    CONSTRAINT ck_seat_inventory_sum CHECK (available + held + booked = total)
);

CREATE TABLE flight_seat (
    flight_id    BIGINT       NOT NULL REFERENCES flight(id) ON DELETE CASCADE,
    seat_no      VARCHAR(5)   NOT NULL,
    class        VARCHAR(20)  NOT NULL DEFAULT 'ECONOMY',
    status       VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    price_factor DECIMAL(4,2) DEFAULT 1.0,
    PRIMARY KEY (flight_id, seat_no),
    CONSTRAINT ck_seat_class  CHECK (class  IN ('ECONOMY','BUSINESS','FIRST')),
    CONSTRAINT ck_seat_status CHECK (status IN ('AVAILABLE','HELD','BOOKED'))
);
