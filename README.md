## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.0.6

## Data
Example of a Vulnerability data JSON object:
```json
{
    "id":1,
    
    "title":"XSS Vulnerability",
    
    "score":"6.9",
    
    "product":"WordPress",

    "deleted": false
}
```

## Requirements
Assume there is vulnerability database and you want to create a REST API to access them.


You have to implement `/vulnerability` REST endpoint for following 3 operations.

`GET` request to `/vulnerability/{id}`:
* return the vulnerability with given id and status code 200
* if the requested vulnerability doesn't exist, then status code 404 should be returned

`DELETE` request to `/vulnerability/{id}`:
* delete the vulnerability with give id and return status code 200
* by "deleted" means, the vulnerability is logically deleted - not completely removed from database.
* if the vulnerability doesn't exist in the database it should return status code 404


`GET` request to `/vulnerability/search/{product}?orderBy={sortBy)`:
* return the vulnerabilities filtered by product and sorted by given column with status code 200
* when the given order by column doesn't exist, return status code 400
 
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