package com.macys.selection.xapi.list.customer.stepdefs;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class    MergeLists {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeLists.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer logs in and merge initiated$")
    public void a_customer_logs_in_and_merge_initiated() {
        request = given();
    }

    @When("^lists are merged successfully$")
    public void lists_are_merged_successfully() throws IOException {
        String body = readFile("com/macys/selection/xapi/list/customer/stepdefs/mergeLists.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getMergeListsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @When("^lists are merged with error from customer")
    public void lists_are_merged_with_error() throws IOException {
        String body = readFile("com/macys/selection/xapi/list/customer/stepdefs/mergeListsWithError.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getMergeListsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^the merge status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^the response contains merge error from customer$")
    public void response_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Please check your input."));
        json.body("errors.error[0].message", equalTo("We're experiencing a technical glitch. Please try again later or call us at 1-800-BUY-MACY (289-6229) for immediate assistance. "));
    }
}
