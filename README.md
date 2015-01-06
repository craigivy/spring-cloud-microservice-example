spring-cloud-microservice-example
=================================

Spring cloud microservice example using Netflix OSS.
Currently uses eureka, ribbon, feign, and hystrix from the Netflix OSS stack.

Modules
-------

### eureka
Starts the eureka server for service discovery (<http://localhost:8761/> user=user, password=password)

### service
A microservice for storing small files (<http://localhost:8081/remoteFiles/>)
- Uses MongoDB for persistence of data and files
- Registers remote file service with Eureka

### ui
A simple UI to upload and download files (<http://localhost:9090/>)
- Uses Spring MVC and Thymeleaf template
- Uses restTemplate and feign to lookup service from eureka via ribbon
- Uses Hystrix to create a circuit breaker for get files,  Default returns an empty list.

Prerequisite
------------
- Install and configure maven.
- Install and start mongodb.

Run
---
Compile and run each module by executing the command "mvn spring-boot:run"
to run on another port use "spring-boot:run -Dserver.port=[port]"


Experimental
-------------
Create a docker instance.  Currently this is supported in the service module using the spotify docker plugin.
Run "mvn clean package docker:build" docker target is created.