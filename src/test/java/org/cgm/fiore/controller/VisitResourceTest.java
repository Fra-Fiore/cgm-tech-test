package org.cgm.fiore.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VisitResourceTest {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get("/visit")
                .then()
                .body("size()", CoreMatchers.equalTo(1))
                .body("id", CoreMatchers.hasItem(1))
                .body("type", CoreMatchers.hasItem("HOME_VISIT"))
                .body("reason", CoreMatchers.hasItem("URGENT"));
    }

    @Test
    @Order(1)
    void getById() {
        given()
                .when()
                .get("/visit/1")
                .then()
                .body("id", CoreMatchers.equalTo(1))
                .body("type", CoreMatchers.equalTo("HOME_VISIT"))
                .body("reason", CoreMatchers.equalTo("URGENT"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByIdKO() {
        given()
                .when()
                .get("/visit/10")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(3)
    void update() {
        JsonObject visit = Json.createObjectBuilder()
                .add("type", "DOCTOR_OFFICE")
                .add("reason", "RECURRING")
                .add("date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("familyHistory", "EDITED - Lorem ipsum dolor sit amet")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(visit.toString())
                .when()
                .put("/visit/1")
                .then()
                .body("type", CoreMatchers.equalTo("DOCTOR_OFFICE"))
                .body("reason", CoreMatchers.equalTo("RECURRING"))
                .body("familyHistory", CoreMatchers.containsString("EDITED"))
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .when()
                .get("/visit/1")
                .then()
                .body("id", CoreMatchers.equalTo(1))
                .body("type", CoreMatchers.equalTo("DOCTOR_OFFICE"))
                .body("reason", CoreMatchers.equalTo("RECURRING"))
                .body("familyHistory", CoreMatchers.containsString("EDITED"))
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @Test
    @Order(4)
    void deleteById() {
        given()
                .when()
                .delete("/visit/1")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(4)
    void deleteByIdKO() {
        given()
                .when()
                .delete("/visit/10")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}