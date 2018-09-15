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
public class GetRequestsByBlankUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with blank userGuid \"([^\"]*)\" without any requests$")
    public void a_customer_with_requests_with_blank_userGuid(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets list of request by blank userGuid$")
    public void a_customer_retrieves_list_of_requests_by_blank_userGuid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getRequestsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get requests for user with blank userGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains requests by blank userGuid error$")
    public void contains_requests_by_incorrect_userGuid_error() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
    }
}
