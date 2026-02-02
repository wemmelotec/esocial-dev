FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN ./mvnw spring-boot:run
