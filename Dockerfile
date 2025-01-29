FROM openjdk:17-jdk-alpine
VOLUME /gapp
COPY target/groceryapi*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]