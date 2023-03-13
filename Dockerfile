FROM openjdk:17.0.1-jdk-slim

ARG JAR_FILE=build/libs/*SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} yandex-lavka.jar

ENTRYPOINT ["java","-jar","yandex-lavka.jar"]
