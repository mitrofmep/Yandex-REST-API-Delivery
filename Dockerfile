FROM openjdk:17.0.1-jdk-slim

EXPOSE 8080

ENV POSTGRES_SERVER=db
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password

WORKDIR /opt/app

ENTRYPOINT ["java","-jar", "./build/libs/yandex-lavka-0.0.1-SNAPSHOT.jar"]
