package com.macys.selection.xapi.list.customer.stepdefs.GetRequestsAndApprovals;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getApprovalsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetApprovalsFromOneList {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" with some approvals for one list$")
    public void a_customer_with_approvals_from_one_list(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets list of approvals for one list$")
    public void a_customer_retrieves_list_of_approvals_from_onel_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getApprovalsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get approvals for one list status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains approvals for one list data$")
    public void contains_list_of_approvals_from_one_list_data() {
        json.body("approvalsResponse.lists[0].listGuid", equalTo("a78a1ed1-d7c0-4aae-90940-2"));
        json.body("approvalsResponse.lists[0].name", equalTo("Collaborator list 1"));
        json.body("approvalsResponse.lists[0].collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("approvalsResponse.lists[0].collaborators[0].profilePicture", equalTo("avatar_image_1"));
        json.body("approvalsResponse.lists[0].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("approvalsResponse.lists[0].collaborators[0].lastName", equalTo("User365"));

        json.body("approvalsResponse.lists[0].collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("approvalsResponse.lists[0].collaborators[1].profilePicture", equalTo("avatar_image_2"));
        json.body("approvalsResponse.lists[0].collaborators[1].lastName", equalTo("User366"));
    }
}
