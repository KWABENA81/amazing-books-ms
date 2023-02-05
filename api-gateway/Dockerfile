#   Dockerfile for api-gateway server
#
FROM openjdk:11

ENV JAVA_VERSION=11.0.16

ENV JAVA_HOME=/usr/local/openjdk-11

ARG VERSION=0.0.1

#   label image
LABEL Description="api-gateway Server" Version="${VERSION}"

VOLUME /tmp

ADD target/api-gateway-"${VERSION}"-SNAPSHOT.jar gateway-app.jar

#
RUN sh -c 'touch /gateway-app.jar'

EXPOSE 8181

#   entrypoint
ENTRYPOINT ["java", "-jar", "/gateway-app.jar"]