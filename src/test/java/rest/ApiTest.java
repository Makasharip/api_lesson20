package rest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ApiTest extends TestBase {

    private String createBody = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
    private String updateBody = "{ \"name\": \"Lisa\", \"job\": \"model\" }";
    private String incorrectUpdateBody = "{ \"name\": , \"job\":  }";

    @Test
    void successfulUserCreateTest(){
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(createBody)
                .contentType(JSON)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }


    @Test
    void successfulUserUpdateTest(){
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(updateBody)
                .contentType(JSON)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("model"));
    }

    @Test
    void negativeUserUpdateTest(){
        given()
                .log().uri()
                .log().method()
                .log().body()
                .body(incorrectUpdateBody)
                .contentType(JSON)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void getAllPantoneColorsTest(){
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("per_page", is(6));
    }

    @Test
    void getSingNotFoundTest(){
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/api/unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}