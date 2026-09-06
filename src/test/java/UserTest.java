import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.UserPOJO;
import serviceobjects.UserService;
import utils.ExtentReportManager;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;


@Listeners(ExtentReportManager.class) // Forces TestNG to trigger listener even without testng.xml
public class UserTest extends BaseTest {

    @DataProvider(name = "userDataProvider")
    public Object[][] getUserData() {
        return utils.JsonReaderUtils.getJsonData("src/test/resources/testData/users.json", UserPOJO[].class);
    }

    @Test(priority = 1, dataProvider = "userDataProvider")
   public void testCreateUsersFromJSON(UserPOJO userPayload) {
        ExtentReportManager.logInfo("Executing Data-Driven Test for: " + userPayload.getFirst_name());

        Response response = UserService.createUser(userPayload);

        // Accept either 200 OK or 201 Created depending on server setup
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201, 
            "Expected status code 200 or 201 but got: " + response.getStatusCode());

        Assert.assertEquals(response.jsonPath().getString("first_name"), userPayload.getFirst_name());  ;
        Assert.assertEquals(response.jsonPath().getString("email"), userPayload.getEmail());
        Assert.assertNotNull(response.jsonPath().getString("id"), "Server failed to generate auto ID!");
    }
    // Test 2: End-to-End User CRUD Lifecycle
    @Test(priority = 2)
    public void testEndToEndUserLifecycle() {
        // Step 1: CREATE User
        UserPOJO newUser = new UserPOJO("Mohammed","Rasik", "rasik.mohammed@protonmail.com", "QA Lead");
        Response createResponse = UserService.createUser(newUser);
        
        Assert.assertTrue(createResponse.getStatusCode() == 200 || createResponse.getStatusCode() == 201);
        String createdId = createResponse.jsonPath().getString("id");
        ExtentReportManager.logInfo("Created User ID for Lifecycle Test: " + createdId);

        // Step 2: GET User
        Response getResponse = UserService.getUserById(createdId);
        Assert.assertEquals(getResponse.getStatusCode(), 200);
        Assert.assertEquals(getResponse.jsonPath().getString("email"), "rasik.mohammed@protonmail.com");

        // Step 3: UPDATE User
        UserPOJO updatedPayload = new UserPOJO("Mohammed","Rasik","rasik.mohammed@protonmail.com", "QA Manager");
        Response updateResponse = UserService.updateUser(createdId, updatedPayload);
        Assert.assertEquals(updateResponse.getStatusCode(), 200);
        Assert.assertEquals(updateResponse.jsonPath().getString("job"), "QA Manager");

        // Step 4: DELETE User
        Response deleteResponse = UserService.deleteUser(createdId);
        Assert.assertTrue(deleteResponse.getStatusCode() == 200 || deleteResponse.getStatusCode() == 204, "Expected status code 200 or 204 but got: " + deleteResponse.getStatusCode());

        // Step 5: VERIFY DELETED User (Should return 404 Not Found)
        Response verifyDeleteResponse = UserService.getUserById(createdId);
        Assert.assertEquals(verifyDeleteResponse.getStatusCode(), 404);
    }
}