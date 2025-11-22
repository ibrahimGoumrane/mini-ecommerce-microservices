# Use official OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy build output (JAR file)
COPY target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service.jar

# Expose port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]