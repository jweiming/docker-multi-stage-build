FROM maven:3.5.2-jdk-8 as BUILD

# build & dependencies container
LABEL maintainer "j_weiming@hotmail.com"
LABEL image "dependencies"

COPY src /usr/src/myapp/src
COPY pom.xml /usr/src/myapp
RUN mvn -f /usr/src/myapp/pom.xml package -DskipTests

#release container
FROM openjdk:8-jre-alpine

ARG BUILD

LABEL image=release
LABEL app=medrec

COPY --from=BUILD /usr/src/myapp/target/* /home/
RUN ls -al /home

EXPOSE 8080

CMD ["java", "-jar", "/home/medrec-0.0.1-SNAPSHOT.jar"]
