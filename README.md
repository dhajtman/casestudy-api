## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.0.6

## Data
Example of a Ordered data JSON object:
```json
{
    "id":1,
       
    "quantity":"6",
    
    "product":"WordPress"
}
```

## Case study
![img.png](img.png)

## Requirements
Assume there is ordered database and you want to create a REST API to access them.


Base on case study implement `/order` REST endpoint.

`GET` request to `/order/{id}`:
* return the order with given id

`DELETE` request to `/ordered/{id}`:
* delete the order with give id

`POST` request to `/order`:
* create order

`Test writing`

In addition to implementing the REST endpoints, you are supposed to write several(at least 5) unit tests to test your implementation.


## Commands
- run: 
```bash
mvn clean spring-boot:run
```
- install: 
```bash
mvn clean install
```
- test: 
```bash
mvn clean test
```