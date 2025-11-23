# ---------- STAGE 1: Build con Gradle ----------
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/project

COPY . .
RUN gradle clean bootJar --no-daemon

# ---------- STAGE 2: Runtime con OpenJDK (Temurin) ----------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
