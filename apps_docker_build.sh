#!/bin/bash
cd ~/Documents/edureka/amazing-books-ms/eureka-service  && mvn clean package && docker build -t kwabena81/eureka-image:0.0.1 . 

cd ~/Documents/edureka/amazing-books-ms/oauth2-service  && mvn clean package && docker build -t kwabena81/oauth2-image:0.0.1 .

cd ~/Documents/edureka/amazing-books-ms/api-gateway && mvn clean package && docker build -t kwabena81/api-gateway-image:0.0.1 . 

cd ~/Documents/edureka/amazing-books-ms/book-microservice && mvn clean package && docker build -t kwabena81/bookms-image:0.0.1 . 

cd ~/Documents/edureka/amazing-books-ms/issuer-microservice && mvn clean package && docker build -t kwabena81/issuerms-image:0.0.1 . 

cd ~/Documents/edureka/amazing-books-ms 
docker-compose up 

