#   Dockerfile for eureka server
#
FROM openjdk:11

ENV JAVA_VERSION=11.0.16

ENV JAVA_HOME=/usr/local/openjdk-11

ARG VERSION=0.0.1

#   label image
LABEL Description="Book Microservice" Version="${VERSION}"

VOLUME /tmp

ADD target/book-microservice-"${VERSION}"-SNAPSHOT.jar book-microservice-app.jar

#
RUN sh -c 'touch /book-microservice-app.jar'

EXPOSE 8761

#   entrypoint
ENTRYPOINT ["java", "-jar", "/book-microservice-app.jar"]
