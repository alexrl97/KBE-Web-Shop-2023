# KBE Web Shop 2023 - Yu-Gi-OH Card Shop

## Preview

![](https://s11.gifyu.com/images/S4hs7.gif)

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
### URL's
#### Swagger UI: http://localhost:8080/swagger-ui.html
#### Frontend: http://localhost:8081/
###
###
### Tests
#### Integration and unit tests are located in the backend/src/test folder. The tests run on another db and fill the db with test data while running the tests. RabbitMQ needs to be run for all integration tests to pass.
###
###
### Required
#####
##### - Docker
##### - MySQL
##### - Vue
##### - Node.js
