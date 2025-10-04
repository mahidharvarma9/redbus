FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy Maven files
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-focal

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/redbus-app-1.0.0.jar app.jar

# Run application
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar", "--spring.profiles.active=docker"]
