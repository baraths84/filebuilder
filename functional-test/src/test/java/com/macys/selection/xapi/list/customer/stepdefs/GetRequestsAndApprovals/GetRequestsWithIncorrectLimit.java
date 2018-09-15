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
public class GetRequestsWithIncorrectLimit {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" with some requests with incorrect limit$")
    public void a_customer_with_requests_for_several_lists_with_incorrect_limit(String userGuid) {
        request = given().queryParams("userGuid", userGuid, "offset", 0, "limit", -1);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets full list of requests with incorrect limit$")
    public void a_customer_retrieves_list_of_requests_with_incorrect_limit() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getRequestsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get requests for several lists with incorrect limit status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains incorrect limit error$")
    public void contains_full_list_of_requests_for_several_lists_data() {
        json.body("errors.error[0].developerMessage", equalTo("Invalid limit"));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
