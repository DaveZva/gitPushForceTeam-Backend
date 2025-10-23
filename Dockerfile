# Multi-stage build pro optimalizaci velikosti image

# Stage 1: Build aplikace
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Zkopírovat pom.xml a stáhnout dependencies (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Zkopírovat zdrojový kód a sestavit
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Zkopírovat JAR z build stage
COPY --from=build /app/target/*.jar app.jar

# Exponovat port
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Spustit aplikaci
ENTRYPOINT ["java", "-jar", "app.jar"]