package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

class MovieControllerRA {

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String adminToken, clientToken, invalidToken;
    private Long existingMovieId, nonExistingMovieId;
    private String movieTitle;

    private Map<String, Object> postMovieInstance;

    @BeforeEach
    public void setup() throws JSONException {
        baseURI = "http://localhost:8080";

        clientUsername = "lucia@gmail.com";
        clientPassword = "123456";
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";

        movieTitle = "The Witcher";

        postMovieInstance = new HashMap<>();
        postMovieInstance.put("score", 4.6);
        postMovieInstance.put("count", 2);
        postMovieInstance.put("title", "Gladiador");
        postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/4DUClyGA6OqjXv6yC0Imf6THGfp.jpg");

    }

    @Test
    void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
                .get("/movies?page=0")
                .then()
                .body("content.title", hasItems("The Witcher", "Venom: Tempo de Carnificina"));
    }

    @Test
    void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        given()
                .get("/movies?title={movieTitle}", movieTitle)
                .then()
                .statusCode(200)
                .body("content.id[0]", is(1))
                .body("content.score[0]", is(4.5f))
                .body("content.count[0]", is(2))
                .body("content.title[0]", equalTo("The Witcher"))
                .body("content.image[0]", is("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
    }

    @Test
    void findByIdShouldReturnMovieWhenIdExists() {

        existingMovieId = 1L;

        given()
                .get("/movies/{id}", existingMovieId)
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("score", is(4.5f))
                .body("count", is(2))
                .body("title", equalTo("The Witcher"))
                .body("image", is("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {

        nonExistingMovieId = 100L;

        given()
                .get("/movies/{id}", nonExistingMovieId)
                .then()
                .statusCode(404)
                .body("error", equalTo("Recurso não encontrado"))
                .body("status", equalTo(404));
    }

    //criar HashMap + headers e token
    @Test
    void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle()  {
        postMovieInstance.put("title", "");
        JSONObject newProduct = new JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(422)
                .body("errors.message[0]", equalTo("Tamanho deve ser entre 5 e 80 caracteres"));
    }

    @Test
    void insertShouldReturnForbiddenWhenClientLogged() {
        org.json.simple.JSONObject newMovie = new org.json.simple.JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/movies")
                .then()
                .statusCode(403);

    }

    @Test
    void insertShouldReturnUnauthorizedWhenInvalidToken() {
        org.json.simple.JSONObject newMovie = new org.json.simple.JSONObject(postMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer" + invalidToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/movies")
                .then()
                .statusCode(401);
    }
}
