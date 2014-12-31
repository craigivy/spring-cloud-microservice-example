spring-cloud-microservice-example
=================================

Spring cloud microservice example using Netfix OSS.

Modules
-------

### eureka
Starts the eureka server for service discovery

### service
A microservice for storing small files
- Uses MongoDB for persistence of data and files
- Registers with Eureka

### ui
A simple UI to upload and download files
- Uses Spring MVC and Thymeleaf template
- Uses restTemplate and feign to lookup service from eureka via ribbon

Prerequisite
------------
- Install and configure maven.
- Install and start mongodb.

Run
---
Compile and run each module by executing the command mvn spring-boot:run
