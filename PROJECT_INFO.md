# Movie Store API

Problem statement is given in the README.md document. 

This document contains the details of how to execute this project 
and steps to verify the API as per the problem statement

##### Build and run the project 
1. mvn clean install
2. Open MovieStoreWebApplication and run the application
3. This will start application locally on port 8084

##### I. Create movie
 
######1. Create successful new movie API

curl --location --request POST 'http://localhost:8084/movie' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Rocky",
"year": 2002,
"rating": "A"
}'

######2. Verify error details when  movie name already exists in Create movie API

curl --location --request POST 'http://localhost:8084/movie' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Rocky",
"year": 2002,
"rating": "A"
}'

######3. Verify error details when  movie name is null in Create movie API

curl --location --request POST 'http://localhost:8084/movie' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": null,
"year": 2002,
"rating": "A"
}' 

######4. Verify error details when  year is null in Create movie API

curl --location --request POST 'http://localhost:8084/movie' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Rocky",
"year": null,
"rating": "A"
}'

##### II. Update movie

######1. Update existing movie API

curl --location --request PUT 'http://localhost:8084/movie/Rocky' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Rocky",
"year": 2003,
"rating": "A"
}'

######2. Verify error details when movie name does not exists in Update movie API
We can only update an existing movie

curl --location --request PUT 'http://localhost:8084/movie/Rocky123' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Rocky",
"year": 2003,
"rating": "A"
}'


##### III. Fetch All movies

######1. Get All movies API

curl --location --request GET 'http://localhost:8084/movies'

##### IV. Fetch All movies by year

######1. Get All movies by year API

curl --location --request GET 'http://localhost:8084/movies?year=2003'

######2. API to Verify error details when year is null

curl --location --request GET 'http://localhost:8084/movies?year='

######3. API to Verify error details when year value is less than 4 digits

curl --location --request GET 'http://localhost:8084/movies?year=123'

######4. API to Verify error details when year value is less than negative digits

curl --location --request GET 'http://localhost:8084/movies?year=-2002'


##### V. Fetch All movies by rating

######1. Get All movies by rating API

curl --location --request GET 'http://localhost:8084/movies?rating=A'

######2. Verify no results when rating is null in Get All movies by rating API

curl --location --request GET 'http://localhost:8084/movies?rating='

##### Database details:

Database used in this project is H2 db
To open the database console in the web browser.
Go to this URL and click on connect:

http://localhost:8084/h2-console/


 Use the below given query to get all data
 
 select * from MOVIE_STORE;

Primary key is NAME column in MOVIE_STORE table