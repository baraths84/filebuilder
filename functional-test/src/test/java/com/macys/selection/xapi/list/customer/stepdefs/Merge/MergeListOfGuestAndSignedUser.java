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

public class MergeListOfGuestAndSignedUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeListOfGuestAndSignedUser.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^guest user with list and signed user with list$")
    public void a_signed_user_merge_list_with_guest() {
        request = given();
    }

    @When("^list of guest user and signed user are merged$")
    public void signed_user_and_guest_lists_are_merged() throws IOException {
        String merge = "{\"wishListRequestDTO\":{\"guestUserId\":80020002,\"userId\":80020004}}";
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(merge)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .post(getMergeListsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^merge lists of guest user and signed user status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

}
