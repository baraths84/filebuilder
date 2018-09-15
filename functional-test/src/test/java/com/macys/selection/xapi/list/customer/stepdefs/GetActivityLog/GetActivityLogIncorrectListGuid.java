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
public class GetActivityLogIncorrectListGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" wants to get activity log of incorrect list \"([^\"]*)\"$")
    public void user_with_userGuid_and_incorrect_listGuid_with_activities(String userGuid, String listGuid) {
        request = given().queryParam("userGuid", userGuid).pathParams("listGuid", listGuid);
        System.out.println("Get activity log request :" + request);
    }

    @When("^owner gets activities by incorrect listGuid$")
    public void user_wants_to_receive_list_activity_log_by_incorrect_listGuid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListActivityLog());
        System.out.println("Customer list activities :" + response.prettyPrint());
    }

    /* New Flow calls:
        1)Check collaborator profile in customer:
          /api/customer/v1/profile/guestUser?userGuid=99901
        2)Get activity log:
          /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99994/activitylog?userGuid=99901&guestUser=false
        */

    @Then("^get activity log by incorrect listGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains incorrect listGuid error$")
    public void contains_incorrect_listGuid_error() {
        json.body("errors.error[0].developerMessage", equalTo("Incorrect list guid"));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
