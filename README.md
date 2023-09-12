# KBE Web Shop 2023 

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
###
### Required
#####

##### - Docker
##### - Vue
##### - Node.js

#
### URL's
#### Swagger UI: http://localhost:8080/swagger-ui.html
#### Frontend: http://localhost:8081/
###

