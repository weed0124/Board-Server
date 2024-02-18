FROM openjdk:17-ea-jdk-slim

VOLUME /tmp

WORKDIR /app

COPY build/libs/spring-mvc-0.0.1-SNAPSHOT.jar /app/spring-mvc.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "spring-mvc.jar"]