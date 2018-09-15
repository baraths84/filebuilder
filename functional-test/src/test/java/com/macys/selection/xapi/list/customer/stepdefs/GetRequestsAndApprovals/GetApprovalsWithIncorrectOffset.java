package com.macys.selection.xapi.list.customer.stepdefs.GetRequestsAndApprovals;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getApprovalsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetApprovalsWithIncorrectOffset {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;


    @Given("^list owner with userGuid \"([^\"]*)\" with some approvals with incorrect offset")
    public void a_customer_with_approvals_with_incorrect_offset(String userGuid) {
        request = given().queryParams("userGuid", userGuid, "offset" , -1, "limit", 100);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets full list of approvals with incorrect offset$")
    public void a_customer_retrieves_list_of_approvals_with_incorrect_offset() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getApprovalsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get approvals for several lists with incorrect offset status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains approvals incorrect offset error$")
    public void contains_list_of_approvals_from_one_list_data() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid offset"));
    }
}
