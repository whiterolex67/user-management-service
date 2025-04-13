# Stage 1: Build the JAR
FROM maven:3.9.5-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y netcat
COPY --from=build /app/target/user-management-service-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]