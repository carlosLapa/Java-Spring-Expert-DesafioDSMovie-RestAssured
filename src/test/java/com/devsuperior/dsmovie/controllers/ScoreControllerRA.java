package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;

public class ScoreControllerRA {

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String adminToken, clientToken, invalidToken;

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


    }

    // Implementar Map
    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
    }
}
