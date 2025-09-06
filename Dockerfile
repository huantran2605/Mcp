# Use a base image with Java
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /

# Copy the jar file
COPY ./target/mcp-app-0.0.1-SNAPSHOT.jar mcp-app-0.0.1-SNAPSHOT.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "mcp-app-0.0.1-SNAPSHOT.jar"]
