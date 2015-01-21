spring-cloud-microservice-example
=================================

Spring cloud microservice example using Netflix OSS.
Currently uses eureka, ribbon, feign, and hystrix from the Netflix OSS stack.

Modules
-------

### eureka
Starts the eureka server for service discovery (<http://localhost:8761/> user=user, password=password)

### service mongodb
A microservice for storing small files (<http://localhost:8081/remoteFiles/>)
- Registers remote file service with Eureka
- Uses MongoDB for persistence of data and files

### service aws
A microservice for storing small files (<http://localhost:8081/remoteFiles/>)
- Registers remote file service with Eureka
- Uses AWS services (DynamoDB and S3) for persistence of data and files
- Setup DynamoDB table named remotefile
- Setup S3 bucket named remotefilesvc
- make sure to set environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_KEY


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
Compile and run each module by executing the command "mvn spring-boot:run".
To run on another port use "spring-boot:run -Dserver.port=[port]"


Docker instance.
-------------
Currently this is supported in the service module using the spotify docker plugin.
Run "mvn clean package docker:build" docker target is created.
In target/Docker directory zip the results of the modules and
Upload the docker zip by creating a docker beanstalk entry for each.

Set eureka's url in the ui and service module with EUREKA_CLIENT_SERVICEURL_DEFAULTZONE environment property.
Set AWS_ACCESS_KEY_ID and AWS_SECRET_KEY in service-aws using environment variables.
For routing purposes you may also need to set EUREKA_INSTANCE_HOSTNAME and EUREKA_INSTANCE_NONSECUREPORT.