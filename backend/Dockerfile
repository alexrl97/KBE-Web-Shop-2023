# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

# Copy the project files and pom.xml to the container
COPY pom.xml .
COPY src/ ./src/

# Run the Maven build inside the container
RUN mvn install -DskipTests

# Stage 2: Create a smaller runtime container
FROM openjdk:17

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=builder /app/target/web_shop-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]