# How to test API?

## Steps

- Run the docker-compose file

```shell
docker-compose up -d
```

- Run Movies Details Service

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- Create a new `movies` realm in the keycloak server
- Create a new user in the keycloak server
- Create client with name `curl`
- Take user JWT token from the keycloak server

```shell
curl -X POST 'http://localhost:7010/auth/realms/master/protocol/openid-connect/token' \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "username=username" \
-d "password=password" \
-d 'grant_type=password' \
-d 'client_id=curl'
```

- Send requests to the API with the JWT token (API can be found in docs/rest-api.yml)
- Example request to check if a movie is nominated for a specific award which didn't seed in the
  database

```shell
curl --location 'http://localhost:8080/movies/nominations/status?movie_title=The%20Revenant&nomination_title=Oscar' \
--header 'Authorization: Bearer <JWT_TOKEN>'
```

- Example request to check if a movie is nominated for a specific award which seeded in the
  database

```shell
curl --location 'http://localhost:8080/movies/nominations/status?movie_title=Cavalcade&nomination_title=Best%20Picture' \
--header 'Authorization: Bearer <JWT_TOKEN>'
```

- Example requests to rate movies

```shell
curl --location 'http://localhost:8080/movies/ratings' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <JWT_TOKEN>' \
--data '{
    "movie_id": "a1e66d02-5f0f-4f6f-b00a-77d5d87254cb",
    "stars": 5
}'
curl --location 'http://localhost:8080/movies/ratings' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <JWT_TOKEN>' \
--data '{
    "movie_id": "1de69f8d-c621-48c2-9879-c7cbbe1b4baa",
    "stars": 4
}'
curl --location 'http://localhost:8080/movies/ratings' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <JWT_TOKEN>' \
--data '{
    "movie_id": "927f3c15-6333-4b71-a3ac-a96b9b4a6745",
    "stars": 3
}'
curl --location 'http://localhost:8080/movies/ratings' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <JWT_TOKEN>' \
--data '{
    "movie_id": "b7b4ee6e-96fa-4c3e-b6b4-6adaa18283f2",
    "stars": 2
}'
```

- Example request to get top-rated movies

```shell
curl --location --request GET 'http://localhost:8080/movies/top?limit=3' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <JWT_TOKEN>'
```