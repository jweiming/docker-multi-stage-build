# Using Docker Multi-stage Builds

This is an example for using Docker multi stage build to compile and build your application. The application is a Springboot application microservice that uses H2 embedded database.

As we will be building the application completely in-container, we do not need to have Java or Maven (and related dependencies) in our local environment. Docker will handle those parts for us. Take a look at the ```Dockerfile``` below. 

#### Docker multi-stage build

Two containers will be used during the build process. A Maven container (with openjdk) used for the build and a jre-alpine image used for the release container. In this way, the release container will be smaller in size and do not include all the unnecessary dependencies and files.

```
FROM maven:3.5.2-jdk-8 as BUILD

# build & dependencies container
LABEL maintainer "j_weiming"
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
```

#### Building the app

```
docker build -t medrec:latest .
```

#### Running the app

Start the container.

```
docker run -p 8080:8080 medrec:latest
```

Execute service calls.

```
curl localhost:8080/admin/all
```

#### Removing the container

One of my go-to guides for Docker related commands is this [https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes](https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes).

Have fun.
