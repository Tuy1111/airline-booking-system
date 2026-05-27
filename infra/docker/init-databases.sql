-- Khởi tạo 5 database cho 5 microservices (chạy 1 lần khi container postgres khởi động lần đầu)
-- Database-per-Service pattern: mỗi service sở hữu schema riêng, không cross-DB query

CREATE DATABASE flight_db;
CREATE DATABASE user_db;
CREATE DATABASE booking_db;
CREATE DATABASE payment_db;
CREATE DATABASE notify_db;

-- Grant đầy đủ quyền cho user abs (đã tạo bởi POSTGRES_USER)
GRANT ALL PRIVILEGES ON DATABASE flight_db   TO abs;
GRANT ALL PRIVILEGES ON DATABASE user_db     TO abs;
GRANT ALL PRIVILEGES ON DATABASE booking_db  TO abs;
GRANT ALL PRIVILEGES ON DATABASE payment_db  TO abs;
GRANT ALL PRIVILEGES ON DATABASE notify_db   TO abs;
