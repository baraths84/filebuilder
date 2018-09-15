package com.macys.selection.xapi.list.customer.stepdefs.Merge;

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

import static com.macys.selection.xapi.list.util.TestUtils.getMergeListsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MergeListOfCustomerWithBlankGuestUserId {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeListOfCustomerWithBlankGuestUserId.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with blank guestUserId and user$")
    public void a_customer_without_guestUserId_merge_list_with_guest() {
        request = given();
    }

    @When("^list of customer with blank guestUserId and users list are merged$")
    public void customers_without_guestUserId_and_guest_lists_are_merged() throws IOException {
        String merge = "{\"wishListRequestDTO\":{\"guestUserId\":\"\",\"userId\":80020001}}";
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(merge)
                .post(getMergeListsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^merge lists of customer with blank guestUserId status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^the response of merge with blank guestUserId contains error message$")
    public void response_contains_erro_message() {
        json.body("errors.error[0].developerMessage", equalTo("Please check your input."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }


}
