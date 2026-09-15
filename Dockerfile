FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S stayhub && adduser -S stayhub -G stayhub && \
    mkdir -p /app/uploads && chown -R stayhub:stayhub /app/uploads
COPY --from=builder /app/target/*.jar app.jar
RUN chown stayhub:stayhub app.jar
USER stayhub
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
