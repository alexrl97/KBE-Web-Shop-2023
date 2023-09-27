# KBE Web Shop 2023 - Yu-Gi-OH Card Shop

## Preview

![screen-capture (4) (2)-min (1)](https://github.com/alexrl97/KBE-Web-Shop-2023/assets/72817183/ea6b40a2-4e55-4135-a96b-9d75843045a6)

### Note: Added Search Bar Feature after recording
<img width="430" alt="Screenshot_1" src="https://github.com/alexrl97/KBE-Web-Shop-2023/assets/72817183/7c6aa5cf-a1ac-4ef5-b621-445fafca96e7">

## Project setup

To run the web shop first the backend and then the frontend needs to be started. 
This can either be done by running the start_web_shop.sh script 
or by executing the commands manually.

### Script:

To run the script open a bash terminal, navigate to the root directory of the project
and run the following command:

```
./start_web_shop.sh
```
###
### Manual start:

To execute the commands manually open a terminal, navigate to the root directory of the project 
and run the following commands:

```
cd backend
```
```
docker-compose build
```
```
docker-compose up -d
```
```
cd ..
```
```
cd frontend
```
```
npm install
```
```
npm run serve
```

#
### Sample Data: Getting started
###
#### Add customer and storehouse user
To get the web shop running and test the order and shipping functionality the users need to be created
in the frontend. Then the role of one user needs to be changed to 'storehouse' in the MySQL database. 
More storehouse users can be added in the frontend using the new storehouse user account.

#### Add categories and products
You can either use the add functions for categories and products logging in with the storehouse user or
use the token of the storehouse user available in the tokens table
to add sample data for categories and products in the swagger UI. 
Add the categories first since a category is mandatory to add a product.

#
## Tests
### Frontend
The Frontend is tested using Cypress for End 2 End testing. For the tests to fully pass you need a customer and a storehouse user with the credentials storehouse@mail.de and pw storehouse and a customer user with customer@mail.de and pw customer. The tests are located in the "frontend/tests/e2e/specs" folder and can be run using the cypress ui using the following command in the frontend folder. 
```
npx cypress open
```
![ezgif-4-88ff2dca4d](https://github.com/alexrl97/KBE-Web-Shop-2023/assets/72817183/96c46034-0ba1-4384-b979-13095772083e)
###
### Backend
Integration and unit tests are located in the backend/src/test folder. The tests run on another db and fill the db with test data while running the tests. RabbitMQ needs to be run for all integration tests to pass. Currently there are 99 integration and 44 unit tests.
###
## Databases
There are two databases used. The live database can be run from the docker container and is located on port 3307. The testing backend can be run using the following command in the backend folder which connects to the root database on port 3306. 
```
./mvnw spring-boot:run
```
### URL's
#### Swagger UI: http://localhost:8080/swagger-ui.html
#### Frontend: http://localhost:8081/
###
###
### Required
#####
##### - Docker
##### - MySQL
##### - Vue
##### - Node.js
