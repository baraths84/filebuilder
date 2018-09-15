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
public class DeleteCollaborativeListByUserIdAndUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with collaborative listguid \"([^\"]*)\" userid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
    public void a_customer_with_collaborative_listguid_userId_and_userGuid(String listguid, String userId, String userGuid) {
        request = given().pathParam("listguid", listguid).queryParam("userId", userId).queryParam("userGuid", userGuid);
    }

    @When("^customer tries to delete collaborative list by userId and userGuid$")
    public void user_delete_collaborative_list_by_userId_and_userGuid$() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getGetCollaborativeListByGuidEndpoint());
        System.out.println("Delete Collaborate list response" + response.prettyPrint());
    }

    @Then("the delete collaborative list status code by userId and userGuid is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response includes the corresponding error$")
    public void response_contains_delete_collaborative_list_error() {
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
