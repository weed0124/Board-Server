FROM openjdk:17-ea-jdk-slim

ARG DEFAULT_PORT=8080

ENV PORT $DEFAULT_PORT

COPY ./build /app/build

WORKDIR /app

EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "/app/build/libs/spring-mvc-0.0.1-SNAPSHOT.jar"]