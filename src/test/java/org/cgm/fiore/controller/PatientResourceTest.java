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
class PatientResourceTest {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get("/patient")
                .then()
                .body("size()", CoreMatchers.equalTo(2))
                .body("id", CoreMatchers.hasItems(1, 2))
                .body("name", CoreMatchers.hasItems("Name 1", "Name 2"))
                .body("ssn", CoreMatchers.hasItems("SSN1", "SSN2"));
    }

    @Test
    @Order(1)
    void getById() {
        given()
                .when()
                .get("/patient/1")
                .then()
                .body("id", CoreMatchers.equalTo(1))
                .body("name", CoreMatchers.equalTo("Name 1"))
                .body("ssn", CoreMatchers.equalTo("SSN1"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByIdKo() {
        given()
                .when()
                .get("/patient/10")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(2)
    void add() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "NewName")
                .add("surname", "NewSurname")
                .add("birthDate", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("ssn", "NEW-SSN")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .post("/patient")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    void updatePatient() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "EditedName")
                .add("surname", "EditedSurname")
                .add("birthDate", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("ssn", "EDITED-SSN")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/patient/2")
                .then()
                .body("name", CoreMatchers.equalTo("EditedName"))
                .body("ssn", CoreMatchers.equalTo("EDITED-SSN"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void addVisit() {
        JsonObject visit1 = Json.createObjectBuilder()
                .add("type", "HOME_VISIT")
                .add("reason", "URGENT")
                .add("date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("familyHistory", "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor")
                .build();
        JsonObject visit2 = Json.createObjectBuilder()
                .add("type", "DOCTOR_OFFICE")
                .add("reason", "RECURRING")
                .add("date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("familyHistory", "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(visit1.toString())
                .when()
                .put("/patient/1/visits")
                .then()
                .body("size()", CoreMatchers.equalTo(1))
                .body("type", CoreMatchers.hasItem("HOME_VISIT"))
                .body("reason", CoreMatchers.hasItem("URGENT"))
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(visit2.toString())
                .when()
                .put("/patient/1/visits")
                .then()
                .body("size()", CoreMatchers.equalTo(2))
                .body("type", CoreMatchers.hasItems("HOME_VISIT", "DOCTOR_OFFICE"))
                .body("reason", CoreMatchers.hasItems("URGENT", "RECURRING"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void addVisitKO() {
        JsonObject visit1 = Json.createObjectBuilder()
                .add("type", "HOME_VISIT")
                .add("reason", "URGENT")
                .add("date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
                .add("familyHistory", "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(visit1.toString())
                .when()
                .put("/patient/10/visits")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @Order(4)
    void getVisits() {
        given()
                .when()
                .get("/patient/2/visits")
                .then()
                .body("size()", CoreMatchers.equalTo(1))
                .body("type", CoreMatchers.hasItem("HOME_VISIT"))
                .body("reason", CoreMatchers.hasItem("URGENT"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(5)
    void deleteVisit() {
        given()
                .when()
                .delete("/patient/2/visits/1")
                .then()
                .body("size()", CoreMatchers.equalTo(0))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(5)
    void deleteVisitKO() {
        given()
                .when()
                .delete("/patient/2/visits/10")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(6)
    void deleteById() {
        given()
                .when()
                .delete("/patient/2")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(6)
    void deleteByIdKO() {
        given()
                .when()
                .delete("/patient/10")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}