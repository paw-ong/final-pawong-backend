# Build stage
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# 캐시 효율을 위해 build.gradle, settings.gradle 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

# 소스 복사 & bootJar 생성
COPY . .
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]