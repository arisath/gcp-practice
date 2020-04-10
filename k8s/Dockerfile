FROM openjdk:8

RUN useradd -ms /bin/bash spring

USER spring:spring

ARG JAR_FILE=target/*.jar

ENV GOOGLE_APPLICATION_CREDENTIALS=lunar-carving-270816-1369989d87f2.json

COPY src/main/resources/$GOOGLE_APPLICATION_CREDENTIALS /

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]