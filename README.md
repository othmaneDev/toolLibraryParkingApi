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
   
# How to build and run the API
- First you need to build the code with this command : `mvn clean install`
- Then you can run the application using the following command: `mvn spring-boot:run`
- The swagger generated documentation can be found at : `http://localhost:7008/tollParkingLibraryApi/swagger-ui.html`
# API REST endpoints
- `POST :/initializeLibraryWithConfig` : This action aims to initialize the toll parking library api with a given configuration.Thus, the enduser need to provide the following information :
  - The number of parking slots available for each type of car : STANDARD, ELETRIC_CAR_20KW and ELETRIC_CAR_50KW
  - A pricing policy wich consist of a fixed amount and a price per hours spent in the parking.
- `PUT : /changePricingPolicy` : This action aims to change the initial pricing policy only if given price per hours is greated than zero
- `GET : /enterParking/{customerCarPlateNumber}` Given a car plate number, this action returned a parking slot if available
- `GET : /leaveParking/{customerCarPlateNumber}` Given a car plate number, this action returned a parking bill for the endUser and free the related parking slot
