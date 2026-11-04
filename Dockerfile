# Этап 1: Сборка приложения
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Собираем проект без тестов (тесты мы уже прогнали локально)
RUN mvn clean package -DskipTests

# Этап 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/book-catalog-0.0.1-SNAPSHOT.jar app.jar
# Запускаем с профилем dev (PostgreSQL)
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]