package com.macys.selection.xapi.list.customer.stepdefs.Merge;

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

public class MergeListOfGuestAndGuest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeListOfGuestAndGuest.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^guest user with list and another guest user with list$")
    public void a_customer_merge_list_with_guest() {
        request = given();
    }

    @When("^list of two guest users are merged$")
    public void customers_and_guest_lists_are_merged() throws IOException {
        String merge = "{\"wishListRequestDTO\":{\"guestUserId\":80020001,\"userId\":80020002}}";
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(merge)
                .post(getMergeListsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^merge lists of guest users status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

}
