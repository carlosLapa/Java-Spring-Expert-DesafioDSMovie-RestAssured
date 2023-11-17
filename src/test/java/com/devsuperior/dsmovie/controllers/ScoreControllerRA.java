package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

class ScoreControllerRA {

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String adminToken, clientToken, invalidToken;

    private Map<String, Object> saveMovieScore;

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

        saveMovieScore = new HashMap<>();
        saveMovieScore.put("movieId", 1000L);
        saveMovieScore.put("score", 4);

    }

    @Test
    void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {

        JSONObject newMovieScore = new JSONObject(saveMovieScore);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newMovieScore)
                .put("/scores")
                .then()
                .statusCode(404);
    }

    @Test
    void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
        saveMovieScore.put("movieId", null);
        JSONObject newMovieScore = new JSONObject(saveMovieScore);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newMovieScore)
                .put("/scores")
                .then()
                .statusCode(422);
    }

    @Test
    void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
        saveMovieScore.put("score", -1.1);
        JSONObject movieScore = new JSONObject(saveMovieScore);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(movieScore)
                .put("/scores")
                .then()
                .statusCode(422);
    }
}
