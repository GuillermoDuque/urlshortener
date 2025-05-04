FROM eclipse-temurin:21-jdk-alpine
COPY target/urlshortener-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
