FROM eclipse-temurin:21.0.3_9-jdk as build
COPY backend/ ./
RUN ./gradlew build

FROM eclipse-temurin:21.0.3_9-jdk

COPY --from=build /build/libs/web-2024-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
CMD ["java", "-Djava.awt.headless=true", "-jar", "/opt/app/japp.jar"]