FROM eclipse-temurin:21.0.3_9-jdk as build
COPY backend/ ./
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21.0.3_9-jdk-jammy

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*
COPY --from=build /build/libs/web-2024-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
CMD ["java", "-jar", "/opt/app/japp.jar"]