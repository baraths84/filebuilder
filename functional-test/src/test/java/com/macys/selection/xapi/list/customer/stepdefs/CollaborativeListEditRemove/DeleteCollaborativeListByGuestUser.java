package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListEditRemove;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class DeleteCollaborativeListByGuestUser {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a collaborative list with listGuid \"([^\"]*)\" and guest userId \"([^\"]*)\"$")
    public void a_customer_with_collaborative_listguid_and_userGuid(String listGuid, String userGuid) {
        request = given().pathParam("listguid", listGuid).queryParam("userGuid", userGuid);
        System.out.println("Delete Collaborate list request" + request);
    }

    @When("^guest user tries to delete collaborative list$")
    public void user_delete_collaborative_list_by_userGuid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getGetCollaborativeListByGuidEndpoint());
        System.out.println("Delete Collaborate list response" + response.prettyPrint());
    }

    @Then("^the delete collaborative list by guest user status code with user is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains remove collaborative list by guest user error$")
    public void response_contains_delete_collaborative_list_error() {
        json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
