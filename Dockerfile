# Build
FROM gradle:7.4-jdk17 as build

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle clean build --no-daemon -x test

# Run
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar

WORKDIR /app

COPY --from=build /app/${JAR_FILE} /app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/LoginLab-0.0.1-SNAPSHOT.jar"]