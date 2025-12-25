# Frontend Build Stage
FROM node:12 AS frontend
WORKDIR /frontend
# Copy package files first for caching
COPY src/main/resources/admin/admin/package.json .
# Install dependencies (using Taobao registry for speed if needed, but standard is fine)
RUN npm install --registry=https://registry.npmmirror.com
# Copy source code
COPY src/main/resources/admin/admin/ .
# Build
RUN npm run build

# Backend Build Stage
FROM maven:3.8-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
# Optimization: Download dependencies first. 
# This layer will be cached unless pom.xml changes, speeding up future builds significantly.
RUN mvn dependency:resolve

COPY src ./src
# Copy compiled frontend assets to the location Spring Boot expects (classpath:/admin/admin/)
# This maps http://localhost:8080/springbootpx13e/admin/index.html -> src/main/resources/admin/admin/index.html
COPY --from=frontend /frontend/dist ./src/main/resources/admin/admin/

# Build the application
RUN mvn package -DskipTests

# Run Stage
FROM eclipse-temurin:8-jre-alpine
LABEL maintainer="springbootpx13e-docker"
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/springbootpx13e-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
