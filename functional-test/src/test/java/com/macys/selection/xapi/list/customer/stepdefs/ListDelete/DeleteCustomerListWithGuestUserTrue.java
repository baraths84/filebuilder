package com.macys.selection.xapi.list.customer.stepdefs.ListDelete;

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

import static com.macys.selection.xapi.list.util.TestUtils.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCustomerListWithGuestUserTrue {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerListWithGuestUserTrue.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with with listGuid \"([^\"]*)\" and guest userId \"([^\"]*)\"$")
    public void a_guest_user_with_listguid(String listguid, String userId) {
        request = given().pathParam("listGuid", listguid).queryParam("userId", userId);
    }

    @When("^guest user tries to delete list$")
    public void a_customer_tries_delete_list_by_userId_and_userGuid() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getListByGuidEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^delete list by guest status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^delete list by guest causes error$")
    public void incorrect_request_message_in_the_response() {
        json.body("errors.error[0].developerMessage", equalTo("User do not have sufficent credentials to perform this operation"));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
