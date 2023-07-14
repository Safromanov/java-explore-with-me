FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-server.jar
ENTRYPOINT ["java","-jar","/app.jar"]