package com.macys.selection.xapi.list.customer.stepdefs.GetLists;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCustomerAllLists;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListsByIncorrectUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with incorrect userGuid \"([^\"]*)\" trying to get lists data$")
    public void a_customer_with_incorrect_userGuid_gets_lists_data(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer with incorrect userGuid trying to receive data about lists$")
    public void a_customer_want_to_get_lsts_data_by_incorrect_userGuid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get lists data by incorrect userGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains incorrect userGuid for lists error message$")
    public void contains_invalid_userGuid_error() {
        json.body("errors.error[0].developerMessage", equalTo("Invalid User ID."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }

}
