# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy the maven wrapper and pom files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Copy all module poms
COPY entos-identity/pom.xml entos-identity/
COPY entos-infra/pom.xml entos-infra/
COPY entos-api/pom.xml entos-api/
COPY entos-shared/pom.xml entos-shared/
COPY entos-cafe/pom.xml entos-cafe/
COPY entos-social/pom.xml entos-social/
COPY entos-platform/pom.xml entos-platform/

# Resolve dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy source code and build
COPY . .
# Check if the file exists during build (Optional debug line)
RUN find . -name "EntosApplication.java"
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN useradd -ms /bin/bash entosuser
USER entosuser

# Copy the built jar from the 'api' module (or whichever is your starter)
COPY --from=build /app/entos-api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]