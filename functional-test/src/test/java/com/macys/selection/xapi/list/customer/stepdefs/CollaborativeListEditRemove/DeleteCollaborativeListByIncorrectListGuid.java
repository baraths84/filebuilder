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
public class DeleteCollaborativeListByIncorrectListGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a collaborative list with incorrect listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
    public void a_customer_with_collaborative_listguid_and_missed_userId(String listGuid, String userGuid) {
        request = given().pathParam("listguid", listGuid).queryParam("userGuid", userGuid);
        System.out.println("Delete Collaborate list request" + request);
    }

    @When("^customer tries to delete collaborative list by incorrect listGuid and userGuid$")
    public void user_delete_collaborative_list_by_missed_userId$() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getGetCollaborativeListByGuidEndpoint());
        System.out.println("Delete Collaborate list response" + response.prettyPrint());
    }

    @Then("the delete collaborative list by incorrect listGuid status code with user is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains remove collaborative list by incorrect listGuid error$")
    public void response_contains_delete_collaborative_list_error() {
        json.body("errors.error[0].developerMessage", equalTo("Incorrect list guid"));
    }
}
