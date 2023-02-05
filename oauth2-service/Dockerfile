#   Dockerfile for oauth2 servic6e
FROM openjdk:11

ENV JAVA_VERSION=11.0.16

ENV JAVA_HOME=/usr/local/openjdk-11

ARG VERSION=0.0.1

#   label image
LABEL Description="Oauth2 Service" Version="${VERSION}"

VOLUME /tmp

ADD target/oauth2-service-"${VERSION}"-SNAPSHOT.jar oauth2-server-app.jar

#
RUN sh -c 'touch /oauth2-server-app.jar'

EXPOSE 9195

#   entrypoint
ENTRYPOINT ["java", "-jar", "/oauth2-server-app.jar"]
