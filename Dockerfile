# syntax=docker/dockerfile:1
# ------------------------------------------------------------------------------
# Dockerfile dùng chung cho MỌI module trong Maven reactor.
# Chọn module cần build qua build-arg MODULE (vd: services/booking-service).
#
#   docker build --build-arg MODULE=services/booking-service -t abs/booking .
#
# docker-compose.prod.yml truyền MODULE cho từng service nên chỉ cần 1 file này.
# ------------------------------------------------------------------------------

# ---- Stage 1: build bằng Maven (không phụ thuộc mvn/java trên host/VPS) -------
FROM maven:3.9-eclipse-temurin-21 AS build
ARG MODULE
# Khớp với spring-boot.version trong parent pom. Bắt buộc ghim vì parent KHÔNG phải
# spring-boot-starter-parent -> plugin không được quản version, mặc định nhảy lên bản mới nhất.
ARG SPRING_BOOT_VERSION=3.5.14
WORKDIR /workspace

# Copy toàn bộ reactor (parent pom + các module). .dockerignore đã loại target/, .git, .env...
COPY pom.xml ./
COPY shared ./shared
COPY infra ./infra
COPY services ./services

# B1: build + install cả module và các module phụ thuộc (shared/common-web) vào ~/.m2.
# B2: chỉ trên module (không -am, nên KHÔNG repackage common-web/library), chạy
#     package + repackage TRONG CÙNG 1 lifecycle để tạo jar thực thi (fat jar).
# Cache ~/.m2 giữa các lần build để tăng tốc (cần BuildKit — mặc định bật trên Docker mới).
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -pl "${MODULE}" -am -DskipTests clean install && \
    mvn -B -ntp -pl "${MODULE}" -DskipTests package \
        org.springframework.boot:spring-boot-maven-plugin:${SPRING_BOOT_VERSION}:repackage

# ---- Stage 2: runtime gọn nhẹ, chỉ JRE ---------------------------------------
FROM eclipse-temurin:21-jre AS runtime
ARG MODULE
WORKDIR /app

# curl để container healthcheck gọi /actuator/health
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

# Lấy đúng jar thực thi của module (spring-boot repackage tạo *.jar, bản gốc là *.jar.original)
COPY --from=build /workspace/${MODULE}/target/*.jar /app/app.jar

# JAVA_OPTS cho phép chỉnh heap khi VPS ít RAM, vd: -e JAVA_OPTS="-Xmx256m"
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
