#   Dockerfile for eureka server
#
FROM openjdk:11

ENV JAVA_VERSION=11.0.16

ENV JAVA_HOME=/usr/local/openjdk-11

ARG VERSION=0.0.1

#   label image
LABEL Description="Issuer Microservice" Version="${VERSION}"

VOLUME /tmp

ADD target/issuer-microservice-"${VERSION}"-SNAPSHOT.jar issuer-microservice-app.jar

#
RUN sh -c 'touch /issuer-microservice-app.jar'

EXPOSE 8761

#   entrypoint
ENTRYPOINT ["java", "-jar", "/issuer-microservice-app.jar"]
