# Movie-API

![movie-api](https://github.com/tykinvalaja/movie-api/blob/main/documentation/api.png)

# Docker

Dockeria käyttäessä ei tarvita muuta kuin alla olevat komennot.

## Käynnistäminen

`docker-compose up`

## Sammuttaminen

`docker-compose down` tai `docker-compose down -v` poistaaksesi volumet.

# Gradle

Aja komennot projektin kansiosssa komentoriviltä. Gradlella ajaminen olettaa, että käyttäjällä on Postgres-tietokanta ja -tunnukset käytössä. Flyway luo skeeman ja taulut.

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
