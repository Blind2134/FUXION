FROM openjdk:24-jdk-slim

ARG JAR_FILE=tarjet/backend-0.0.1-SNAPSHOT.jar


COPY ${JAR_FILE} app_fuxion.jar

EXPOSE 8080

ENTRYPOINT ["java" , "-jar", "app_fuxion.jar"]