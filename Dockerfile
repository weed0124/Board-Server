FROM openjdk:17-ea-jdk-slim

WORKDIR /app

VOLUME /app/tmp

COPY build/libs/spring-mvc-0.0.1-SNAPSHOT.jar /app/spring-mvc.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "spring-mvc.jar"]