FROM openjdk:11-jdk
COPY /target/articleService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080