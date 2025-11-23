# ---------- STAGE 1: Build con Gradle ----------
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/project

# Copiamos todo el proyecto dentro de la imagen
COPY . .

# Construimos el JAR de Spring Boot
RUN gradle clean bootJar --no-daemon

# ---------- STAGE 2: Runtime con OpenJDK ----------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiamos el JAR generado en el stage anterior
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Puerto de ejecución de la API (según application.properties)
EXPOSE 8081

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]