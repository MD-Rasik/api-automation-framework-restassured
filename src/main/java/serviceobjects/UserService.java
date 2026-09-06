package serviceobjects;

import base.BaseTest;
import endpoints.Routes;
import io.restassured.response.Response;
import pojo.UserPOJO;
import utils.ExtentReportManager;

import static io.restassured.RestAssured.given;

public class UserService extends BaseTest {

    public static Response createUser(UserPOJO payload) {
        ExtentReportManager.logInfo("Sending POST request to " + Routes.CREATE_USER + " for user: " + payload.getFirst_name());

        Response response = given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post(Routes.CREATE_USER);

        ExtentReportManager.logInfo("POST Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response getUserById(String userId) {
        ExtentReportManager.logInfo("Sending GET request to " + Routes.GET_USER + " for ID: " + userId);

        if (userId == null || userId.isEmpty()) {
            ExtentReportManager.logError("User ID cannot be null!");
            throw new IllegalArgumentException("Invalid User ID"); 
    }

        Response response = given()
            .spec(requestSpec)
            .pathParam("id", userId)
        .when()
            .get(Routes.GET_USER);

        ExtentReportManager.logInfo("GET Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response updateUser(String userId, UserPOJO payload) {
        ExtentReportManager.logInfo("Sending PUT request to " + Routes.UPDATE_USER + " for ID: " + userId);

        Response response = given()
            .spec(requestSpec)
            .pathParam("id", userId)
            .body(payload)
        .when()
            .put(Routes.UPDATE_USER);

        ExtentReportManager.logInfo("PUT Response Status: " + response.getStatusCode());
        return response;
    }

    public static Response deleteUser(String userId) {
        ExtentReportManager.logInfo("Sending DELETE request to " + Routes.DELETE_USER + " for ID: " + userId);

        Response response = given()
            .spec(requestSpec)
            .pathParam("id", userId)
        .when()
            .delete(Routes.DELETE_USER);

        ExtentReportManager.logInfo("DELETE Response Status: " + response.getStatusCode());
        return response;
    }
}