# Movie-API

![movie-api](https://github.com/tykinvalaja/movie-api/blob/main/documentation/Screenshot%202026-02-22%20at%2011.12.04.png)

# Gradle

Aja komennot projektin kansiosssa komentoriviltä.

## Build

`./gradlew build` 

## Käynnistäminen

Aseta tietokannan url, käyttäjätunnus ja salasana ympäristömuuttujiksi, esim. näin:

`DB_URL=db_url DB_USERNAME=db_username DB_PASSWORD=db_password ./gradlew bootRun`

tai aseta arvot suoraan application.yamliin ja aja

`./gradlew bootRun`

Palvelu käynnistyy osoitteessa https://localhost:8080

## Testien ajaminen

`./gradlew test`

# Docker

Docker-käytössä on valmiina testitunnukset tietokantaa varten.

## Käynnistäminen

`docker-compose up`

## Sammuttaminen

`docker-compose down` tai `docker-compose down -v` poistaaksesi volumet.

# Swagger

http://localhost:8080/swagger-ui/index.html

Palvelua on helpointa käyttää Swaggerin avulla. 

# Curl

Alla pari esimerkkiä

`curl -X GET http://localhost:8080/movies`

`curl -X GET http://localhost:8080/movies/search?title=interstellar`

```
curl -X 'POST' \
  'http://localhost:8080/movies' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "The Matrix",
  "genre": "Science Fiction",
  "releaseYear": 1999,
  "director": "Lana Wachowski",
  "rating": 8.7
}'
```
