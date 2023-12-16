package tests;

import models.*;
import org.junit.jupiter.api.Test;

import static specs.RegisterSpecs.registerRequestSpecs;
import static specs.RegisterSpecs.registerResponseSpecs;
import static specs.UsersSpecs.userRequestSpecs;
import static specs.UsersSpecs.userResponseSpecs;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest extends TestBase {

    @Test
    void checkListOfUsersTest() {
        UserListResponse response = step("Request  list of users", () ->
                given(userRequestSpecs)
                        .when()
                        .get("users?page=2")
                        .then()
                        .spec(userResponseSpecs)
                        .extract().as(UserListResponse.class));

        step("Verify url support", () ->
                assertEquals("https://reqres.in/#support-heading",
                        response.getSupport().getUrl()));
    }

    @Test
    void checkDeleteUserTest() {
        step("Request to delete", () ->
                given(userRequestSpecs)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(userResponseSpecs)
                        .statusCode(204));
    }

    @Test
    void checkUnSuccessfulRegisterTest() {
        RegisterRequestModel regBody = new RegisterRequestModel();
        regBody.setEmail("sydney@fife");

        RegisterResponseModel response = step("Make register request", () ->
                given(registerRequestSpecs)
                        .body(regBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registerResponseSpecs)
                        .extract().as(RegisterResponseModel.class));

        step("Verify Response", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Test
    void checkSuccessfulUpdateTest() {
        UserRequestModel userBody = new UserRequestModel();
        userBody.setName("morpheus");
        userBody.setJob("zion resident");

        UserResponseModel response = step("Make update request", () ->
                given(userRequestSpecs)
                        .body(userBody)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(userResponseSpecs)
                        .extract().as(UserResponseModel.class));

        step("Verify changes", () ->
                assertAll(
                        () -> assertEquals("morpheus", response.getName()),
                        () -> assertEquals("zion resident", response.getJob())
                ));
    }

    @Test
    void checkDelayedResponse() {
        step("Make delayed request", () ->
                given(userRequestSpecs)
                        .when()
                        .get("users?delay=3")
                        .then()
                        .spec(userResponseSpecs)
                        .statusCode(200));
    }
}
