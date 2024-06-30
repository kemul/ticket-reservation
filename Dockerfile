# Stage 1: Build with Maven
FROM maven:3.8.6-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Run with OpenJDK
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/ticket-reservation-0.0.1-SNAPSHOT.jar app.jar

# Copy the wait-for-it script into the container
COPY wait-for-it.sh wait-for-it.sh

# Make the wait-for-it script executable
RUN chmod +x wait-for-it.sh

# Run the wait-for-it script followed by the jar file
ENTRYPOINT ["./wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "app.jar"]
