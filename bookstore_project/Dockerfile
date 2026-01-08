# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies to speed up Render builds
COPY pom.xml .
RUN mvn dependency:go-offline 

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render environment setup
ENV PORT=8082
EXPOSE 8082

# This command forces Spring to use Render's dynamic port
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]