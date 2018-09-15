package com.macys.selection.xapi.list.customer.stepdefs.GetRequestsAndApprovals;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getRequestsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetRequestsForSevaralLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" with some requests for several lists$")
    public void a_customer_with_requests_for_several_lists(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets list of requests for several lists$")
    public void a_customer_retrieves_list_of_requests_for_several_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getRequestsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get requests for several lists status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains requests for several lists data$")
    public void contains_list_of_requests_for_several_lists_data() {
        json.body("requestsResponse.lists.PENDING[0].listGuid", equalTo("a78a1ed1-d7c0-4aae-90902-1"));
        json.body("requestsResponse.lists.PENDING[0].name", equalTo("Collaborative List 901"));
        json.body("requestsResponse.lists.PENDING[0].owner.userGuid", equalTo("2013365-2013365-2013365"));
        json.body("requestsResponse.lists.PENDING[0].owner.profilePicture", equalTo("avatar_image_1"));
        json.body("requestsResponse.lists.PENDING[0].owner.firstName", equalTo("Collaborator"));
        json.body("requestsResponse.lists.PENDING[0].owner.lastName", equalTo("User365"));

        json.body("requestsResponse.lists.PENDING[1].listGuid", equalTo("a78a1ed1-d7c0-4aae-90903-1"));
        json.body("requestsResponse.lists.PENDING[1].name", equalTo("Collaborative List 902"));
        json.body("requestsResponse.lists.PENDING[1].owner.userGuid", equalTo("2013366-2013366-2013366"));
        json.body("requestsResponse.lists.PENDING[1].owner.profilePicture", equalTo("avatar_image_2"));
        json.body("requestsResponse.lists.PENDING[1].owner.firstName", equalTo("Collaborator"));
        json.body("requestsResponse.lists.PENDING[1].owner.lastName", equalTo("User366"));

        json.body("requestsResponse.lists.PENDING[2].listGuid", equalTo("a78a1ed1-d7c0-4aae-90903-2"));
        json.body("requestsResponse.lists.PENDING[2].name", equalTo("Collaborative List 903"));
        json.body("requestsResponse.lists.PENDING[2].owner.userGuid", equalTo("2013366-2013366-2013366"));
        json.body("requestsResponse.lists.PENDING[2].owner.profilePicture", equalTo("avatar_image_2"));
        json.body("requestsResponse.lists.PENDING[2].owner.firstName", equalTo("Collaborator"));
        json.body("requestsResponse.lists.PENDING[2].owner.lastName", equalTo("User366"));
    }
}
