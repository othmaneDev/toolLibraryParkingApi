# Tool parking API

This is a java REST API of a toll parking library built using the following technologies :

  - Java 8
  - Lombok
  - Spring boot (Spring-data-jpa, spring-boot-test)
  - Swagger UI
  - In-memory h2 database

# Purpose
  The application is used to manage a toll parking : 
  
   - Cars of all types come in and out randomly, the API must :
   
        - Send them to the right parking slot or refuse them if there is no slot (of the right type) left.
        - Mark the parking slot as Free when the car leaves it
        - Bill the customer when the car leaves.
		
 The API provides also an in-memory database to store parking slots and parking bills.
 # Prerequisites
   - Java 8 
   - Maven

 # Optionnal
  - Docker installed in your machine
  - An account on the DockerHub in order to retrieve the docker image:https://hub.docker.com/
   
# How to build and run the API locally using maven
- First you need to build the code with this command : `mvn clean install` after downloading and unzip the source code
- Then you can run the application using the following command: `mvn spring-boot:run`
- The swagger generated documentation can be found at : `http://localhost:7008/tollParkingLibraryApi/swagger-ui.html`

# How to build and run the API locally using Docker
- Get the latest release tag for the docker image of the project using this command: `docker pull othmanedev/tool-parking-library:latest`
- Verify that the images is correctly retrieved: `docker images`
- Then you can run the application using the following command: `docker run -d -p 8009:7008 othmanedev/tool-parking-library -m http.server --bind 0.0.0.0`
- The swagger generated documentation can be found at : `http://localhost:8009/tollParkingLibraryApi/swagger-ui.html`
	- Please not that -m http.server --bind 0.0.0.0 is necessary for the user using an Unix based machine to avoid having a connection refused
	- Port forwarding can only connect to a single destination—but you can change where the server process is listening. You do this by listening on 0.0.0.0,           which means “listen on all interfaces”.
	- bind 0.0.0.0 is specifically an option for http.server; it’s not a Docker option. Other servers will have other ways of specifying this

# API REST endpoints
- `POST :/initializeLibraryWithConfig` : This action aims to initialize the toll parking library api with a given configuration.Thus, the enduser need to provide the following information :
  - The number of parking slots available for each type of car : STANDARD, ELETRIC_CAR_20KW and ELETRIC_CAR_50KW
  - A pricing policy wich consist of a fixed amount and a price per hours spent in the parking.
- `PUT : /changePricingPolicy` : This action aims to change the initial pricing policy only if given price per hours is greated than zero
- `GET : /enterParking/{customerCarPlateNumber}` Given a car plate number, this action returned a parking slot if available
- `GET : /leaveParking/{customerCarPlateNumber}` Given a car plate number, this action returned a parking bill for the endUser and free the related parking slot
