# Build stage
FROM gradle:7.4-jdk17 as build
WORKDIR /app

# Copy source code
COPY --chown=gradle:gradle . .

# Build application
RUN gradle build --no-daemon -x test

# Runtime stage
FROM openjdk:17-jdk-slim
EXPOSE 8080
WORKDIR /app

# Copy built application
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Run application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
