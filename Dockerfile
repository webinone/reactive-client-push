FROM openjdk:11-jdk

ARG JAR_FILE
ARG envMode

ENV ENV_MODE $envMode

COPY ./build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=${ENV_MODE}", "-jar", "/app.jar"]