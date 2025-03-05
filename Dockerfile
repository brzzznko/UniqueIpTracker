FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/unique-ip-tracker-1.0.0.jar unique-ip-tracker.jar

ENTRYPOINT ["java", "-jar", "unique-ip-tracker.jar"]
