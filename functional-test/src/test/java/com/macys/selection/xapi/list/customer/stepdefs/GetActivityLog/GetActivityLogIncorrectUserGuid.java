package com.macys.selection.xapi.list.customer.stepdefs.GetActivityLog;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListActivityLog;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetActivityLogIncorrectUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with incorrect userGuid \"([^\"]*)\" wants to get activity log of list \"([^\"]*)\"$")
    public void user_with_incorrect_userGuid_and_listGuid_of_list_with_activities(String userGuid, String listGuid) {
        request = given().queryParam("userGuid", userGuid).pathParams("listGuid", listGuid);
        System.out.println("Get activity log request :" + request);
    }

    @When("^user with incorrect userGuid gets activity log$")
    public void user_wants_to_receive_list_activity_log() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListActivityLog());
        System.out.println("Customer list activities :" + response.prettyPrint());
    }

    /* New Flow calls:
        1)Check collaborator profile in customer:
          /api/customer/v1/profile/guestUser?userGuid=99904
        */

    @Then("^get activity log by incorrect userGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains incorrect userGuid error$")
    public void contains_incorrect_userGuid_error() {
        json.body("errors.error[0].developerMessage", equalTo("Invalid User ID."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
