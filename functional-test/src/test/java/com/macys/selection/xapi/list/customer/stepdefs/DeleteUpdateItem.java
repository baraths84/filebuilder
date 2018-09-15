package com.macys.selection.xapi.list.customer.stepdefs;

import com.macys.selection.xapi.list.util.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.macys.selection.xapi.list.util.TestUtils.getItemByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step definition file for delete item and update item priority.
 **/
public class DeleteUpdateItem {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer to delete/update item with listGuid \"([^\"]*)\" and itemGuid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_itemguid(String listGuid, String itemGuid) {
        request = given().pathParam("listGuid", listGuid).pathParam("itemGuid", itemGuid);
        System.out.println("Delete item from list request" + request);
    }

    @Given("^a customer to delete/update item with listGuid \"([^\"]*)\" and itemGuid \"([^\"]*)\" and userId \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_itemguid_and_userId(String listGuid, String itemGuid, String userId) {
        request = given().pathParam("listGuid", listGuid).pathParam("itemGuid", itemGuid).queryParam("userId", userId);
        System.out.println("Delete item from list request" + request);
    }

    @Given("^a customer to delete/update item with listGuid \"([^\"]*)\" and itemGuid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_itemguid_and_userGuid(String listGuid, String itemGuid, String userGuid) {
        request = given().pathParam("listGuid", listGuid).pathParam("itemGuid", itemGuid).queryParam("userGuid", userGuid);
        System.out.println("Delete item from list request" + request);
    }

    @Given("^a customer to delete/update item with listGuid \"([^\"]*)\" and itemGuid \"([^\"]*)\" and userId \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_itemguid_and_userId_and_userGuid(String listGuid, String itemGuid, String userId, String userGuid) {
        request = given().pathParam("listGuid", listGuid).pathParam("itemGuid", itemGuid).queryParam("userId", userId).queryParam("userGuid", userGuid);
        System.out.println("Delete item from list request" + request);
    }

    @When("^customer deletes item with invalid user$")
    public void customer_deletes_item_with_invalid_user() throws IOException {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .delete(getItemByGuidEndpoint());
        System.out.println("Delete item response" + response.prettyPrint());

        /*
        New Flow:
        GET /api/selection/list/v1/wishlists/6f522f8c-09c7-4306-b9e1-111
        GET /api/customer/v1/profile/guestUser?userid=111
        DELETE /api/selection/list/v1/wishlists/6f522f8c-09c7-4306-b9e1-111/items/f83e486c-15ad-4a28-94dd-111
         */
    }

    @When("^customer updates item priority$")
    public void customer_updates_item() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/updateItemPriority.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .patch(getItemByGuidEndpoint());
        System.out.println("Update item response" + response.prettyPrint());

        /*
        New FLow:
        GET /api/customer/v1/profile/guestUser?userid=201306153
        PATCH /api/selection/list/v1/wishlists/6f522f8c-09c7-4306-b9e1-f331cf2244e6/items/f83e486c-15ad-4a28-94dd-4a9bc6af3639?userId=201306153&userGuid=74eddb1d-a22f-4bb3-b68c-cf445f4d3c3f&guestUser=false
         */
    }

    @Then("^the delete/update item status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^i verify the update item without userId and userGuid$")
     public void error_response_no_user_info_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
    }

    @And("^i verify the update item with both userId and userGuid$")
    public void error_response_both_userId_userGuid_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
    }
}
