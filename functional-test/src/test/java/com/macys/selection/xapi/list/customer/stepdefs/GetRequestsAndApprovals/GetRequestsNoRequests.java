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
public class GetRequestsNoRequests {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" without any requests$")
    public void a_customer_without_requests(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets blank list of request$")
    public void a_customer_retrieves_blank_list_of_requests() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getRequestsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get requests for user without requests status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains blank requests list data$")
    public void contains_list_of_requests_for_one_list_data() {
        json.body("requestsResponse.lists[0].listGuid", equalTo(null));
    }
}
