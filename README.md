# demo-microservice-money-transfer-service
Simple Demo Microservice with REST API for Money Transfer between Accounts

## Problem

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

## Tools/Technologies Used

* Java 8
* Spring Boot
* H2 In-Memory DB
* JUnit, Mockito
* Spring Tool Suite

## API

AccountController:

* /account/all
* /account/id/{id}
* /account/create/id/{id}/deposit/{deposit}
* /account/deposit/id/{id}/amount/{amount}
* /account/withdraw/id/{id}/amount/{amount}
* /account/delete/id/{id}

MoneyTransferController:

* /transfer/from/{from}/to/{to}/amount/{amount}

## Testing Instructions

1. Clone the repository.

2. Issue the following command at target/ directory:

   java -jar money-transfer-service-1.0.jar MoneyTransferServiceApplication
   
3. Open a browser and go to http://localhost:8080/ and call the above API methods.
   
   Or try Postman.

