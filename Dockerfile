FROM java:8
WORKDIR /../../
ADD  target/todos-2.0.0-SNAPSHOT.jar //
EXPOSE 8080
ENTRYPOINT [ "java","-Dspring.profiles.active=default" ,"-jar", "/todos-2.0.0-SNAPSHOT.jar"]
