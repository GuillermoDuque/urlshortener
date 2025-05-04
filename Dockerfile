FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/urlshortener-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
