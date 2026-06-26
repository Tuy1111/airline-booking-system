-- Seed data cho demo

INSERT INTO airport (iata_code, name, city, country) VALUES
  ('HAN', 'Noi Bai International',  'Hanoi',           'VN'),
  ('SGN', 'Tan Son Nhat',           'Ho Chi Minh City','VN'),
  ('DAD', 'Da Nang International',  'Da Nang',         'VN'),
  ('CXR', 'Cam Ranh International', 'Nha Trang',       'VN');

INSERT INTO airline (code, name) VALUES
  ('VN', 'Vietnam Airlines'),
  ('VJ', 'VietJet Air'),
  ('QH', 'Bamboo Airways');

INSERT INTO route (from_airport, to_airport, distance_km) VALUES
  ('HAN', 'SGN', 1166),
  ('SGN', 'HAN', 1166),
  ('HAN', 'DAD',  605),
  ('DAD', 'HAN',  605),
  ('SGN', 'DAD',  608),
  ('DAD', 'SGN',  608);

INSERT INTO flight (flight_no, route_id, airline_code, departure_time, arrival_time, total_seats, base_price, aircraft_type) VALUES
  ('VN201', 1, 'VN', '2026-06-30 06:00:00', '2026-06-30 08:15:00', 180, 1500000, 'A321'),
  ('VN203', 1, 'VN', '2026-06-30 09:30:00', '2026-06-30 11:45:00', 180, 1700000, 'A321'),
  ('VJ521', 1, 'VJ', '2026-06-30 14:00:00', '2026-06-30 16:10:00', 220, 1200000, 'A320'),
  ('QH101', 1, 'QH', '2026-06-30 18:30:00', '2026-06-30 20:40:00', 180, 1400000, 'A320'),
  ('VN202', 2, 'VN', '2026-06-30 08:00:00', '2026-06-30 10:15:00', 180, 1500000, 'A321'),
  ('VN551', 3, 'VN', '2026-06-30 07:00:00', '2026-06-30 08:20:00', 180, 1200000, 'A321');

INSERT INTO seat_inventory (flight_id, total, available, held, booked) VALUES
  (1, 180, 180, 0, 0),
  (2, 180, 180, 0, 0),
  (3, 220, 220, 0, 0),
  (4, 180, 180, 0, 0),
  (5, 180, 180, 0, 0),
  (6, 180, 180, 0, 0);
