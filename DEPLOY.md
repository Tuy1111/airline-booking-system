# Deploy lên VPS bằng Docker

Toàn bộ hệ thống (core infra + backend services + frontend) chạy bằng
**`docker-compose.prod.yml`**. Backend đóng gói qua Dockerfile Maven multi-stage ở root;
frontend đóng gói qua Dockerfile Node/Nginx trong `frontend/frontend`.

## 1. Chuẩn bị VPS (1 lần)

```bash
# Cài Docker Engine + Compose plugin (Ubuntu)
curl -fsSL https://get.docker.com | sh
```

## 2. Lấy code & cấu hình

```bash
git clone <repo-url> airline-booking-system
cd airline-booking-system

cp .env.example .env
nano .env          # điền JWT_SECRET và SEPAY_* (bắt buộc cho payment)
```

## 3. Build & chạy tất cả

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Lần đầu build backend images và frontend image nên hơi lâu; các lần sau nhanh hơn nhờ cache Docker.

Xem trạng thái / log:

```bash
docker compose -f docker-compose.prod.yml ps
docker compose -f docker-compose.prod.yml logs -f booking-service
```

## 4. Cổng dịch vụ

| Thành phần         | Cổng (host)        | Ghi chú                                  |
| ------------------ | ------------------ | ---------------------------------------- |
| **Frontend**       | `80`               | Web đặt vé, proxy API nội bộ qua Nginx   |
| **API Gateway**    | `8080`             | **Điểm vào chính** (route lb qua Eureka) |
| Eureka dashboard   | `8761`             | xem service đã đăng ký                   |
| flight / booking   | `8081` / `8082`    | gọi trực tiếp khi debug                   |
| user / payment     | `8083` / `8084`    |                                          |
| notification       | `8085`             |                                          |
| Postgres           | `5555`             | host 5555 -> container 5432; 5 DB tạo tự động |
| Redis              | nội bộ Docker      | không publish ra host/VPS               |
| Kafka              | `9092`             | chỉ mở nếu cần debug hoặc client ngoài   |
| RabbitMQ UI        | `15672`            | guest/guest                              |
| MailHog UI         | `8025`             | xem email đã gửi                         |

> Khi chạy thật chỉ nên mở public cổng `80` cho frontend. Nếu cần debug API trực tiếp
> mới mở thêm `8080`; các cổng service còn lại nên để nội bộ hoặc bỏ khỏi phần `ports:`.

## 5. Monitoring (tùy chọn)

```bash
docker compose -f docker-compose.prod.yml --profile monitoring up -d --build
```

Thêm Prometheus (`:9090`), Grafana (`:3000`), Zipkin (`:9411`) và đặt
`TRACING_ENABLED=true` trong `.env` để bật trace.
Lưu ý: `infra/docker/prometheus/prometheus.yml` đang scrape `host.docker.internal:80xx`
— khi chạy full trong Docker cần đổi target sang tên container (vd `booking-service:8082`).

## 6. Cập nhật / gỡ

```bash
git pull
docker compose -f docker-compose.prod.yml up -d --build   # build lại con đổi code

docker compose -f docker-compose.prod.yml down             # dừng (giữ data)
docker compose -f docker-compose.prod.yml down -v          # xóa cả DB volume
```

## 7. GitHub Actions Deploy (tuỳ chọn)

Workflow `.github/workflows/deploy.yml` cần các secrets:

- `VPS_HOST`
- `VPS_USER`
- `DEPLOY_PATH`
- Một trong các secret key: `VPS_SSH_KEY` / `VPS_PRIVATE_KEY` / `SSH_PRIVATE_KEY`
  (hoặc dùng password: `VPS_PASSWORD` / `SSH_PASSWORD`)
- `VPS_PORT` (tuỳ chọn, mặc định `22`)

Workflow deploy checkout code trên GitHub runner, nén source rồi SCP sang VPS trước khi chạy
`docker compose`. VPS không cần tự `git fetch` từ GitHub, nên vẫn deploy được khi outbound
`github.com:443` bị chặn.

## Cơ chế (vì sao không sửa source)

Source hardcode `localhost` cho DB/Redis/Eureka. Compose **override bằng biến môi
trường** (Spring relaxed-binding) trỏ sang tên container:
`SPRING_DATASOURCE_URL`, `SPRING_DATA_REDIS_HOST`, `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`,
`KAFKA_BOOTSTRAP=kafka:29092`… Nhờ vậy chạy local `spring-boot:run` vẫn nguyên như cũ.
