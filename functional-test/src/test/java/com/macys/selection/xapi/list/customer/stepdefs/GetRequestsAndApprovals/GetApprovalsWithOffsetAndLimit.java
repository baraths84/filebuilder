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
public class GetApprovalsWithOffsetAndLimit {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" with some approvals and offset = 0 and limit = 100")
    public void a_customer_with_approvals_with_offset_and_limit(String userGuid) {
        request = given().queryParams("userGuid", userGuid, "offset" ,0, "limit", 100);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets full list of approvals for several lists$")
    public void a_customer_retrieves_list_of_approvals_with_offset_and_limit() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getApprovalsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get approvals for several lists with offset and limit status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains all approvals for several lists data$")
    public void contains_list_of_approvals_from_one_list_data() {
        json.body("approvalsResponse.lists[0].listGuid", equalTo("a78a1ed1-d7c0-4aae-90930-2"));
        json.body("approvalsResponse.lists[0].name", equalTo("Collaborator list 1"));
        json.body("approvalsResponse.lists[0].collaborators[0].userGuid", equalTo("2013345-2013345-2013345"));
        json.body("approvalsResponse.lists[0].collaborators[0].profilePicture", equalTo("avatar_image_1"));
        json.body("approvalsResponse.lists[0].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("approvalsResponse.lists[0].collaborators[0].lastName", equalTo("User345"));

        json.body("approvalsResponse.lists[0].collaborators[1].userGuid", equalTo("2013346-2013346-2013346"));
        json.body("approvalsResponse.lists[0].collaborators[1].profilePicture", equalTo("avatar_image_2"));
        json.body("approvalsResponse.lists[0].collaborators[1].lastName", equalTo("User346"));

        json.body("approvalsResponse.lists[0].collaborators[2].userGuid", equalTo("2013347-2013347-2013347"));
        json.body("approvalsResponse.lists[0].collaborators[2].profilePicture", equalTo("avatar_image_1"));
        json.body("approvalsResponse.lists[0].collaborators[2].lastName", equalTo("User347"));

        json.body("approvalsResponse.lists[1].listGuid", equalTo("a78a1ed1-d7c0-4aae-90930-3"));
        json.body("approvalsResponse.lists[1].name", equalTo("Collaborator list 2"));
        json.body("approvalsResponse.lists[1].collaborators[0].userGuid", equalTo("2013345-2013345-2013345"));
        json.body("approvalsResponse.lists[1].collaborators[0].profilePicture", equalTo("avatar_image_1"));
        json.body("approvalsResponse.lists[1].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("approvalsResponse.lists[1].collaborators[0].lastName", equalTo("User345"));

        json.body("approvalsResponse.lists[1].collaborators[1].userGuid", equalTo("2013346-2013346-2013346"));
        json.body("approvalsResponse.lists[1].collaborators[1].profilePicture", equalTo("avatar_image_2"));
        json.body("approvalsResponse.lists[1].collaborators[1].lastName", equalTo("User346"));

        json.body("approvalsResponse.lists[2].listGuid", equalTo("a78a1ed1-d7c0-4aae-90930-1"));
        json.body("approvalsResponse.lists[2].name", equalTo("Collaborator list 3"));
        json.body("approvalsResponse.lists[2].collaborators[0].userGuid", equalTo("2013346-2013346-2013346"));
        json.body("approvalsResponse.lists[2].collaborators[0].profilePicture", equalTo("avatar_image_2"));
        json.body("approvalsResponse.lists[2].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("approvalsResponse.lists[2].collaborators[0].lastName", equalTo("User346"));

        json.body("approvalsResponse.lists[2].collaborators[1].userGuid", equalTo("2013347-2013347-2013347"));
        json.body("approvalsResponse.lists[2].collaborators[1].profilePicture", equalTo("avatar_image_1"));
        json.body("approvalsResponse.lists[2].collaborators[1].lastName", equalTo("User347"));

        json.body("approvalsResponse.lists[3].listGuid", equalTo("a78a1ed1-d7c0-4aae-90930-4"));
        json.body("approvalsResponse.lists[3].name", equalTo("Collaborator list 4"));
        json.body("approvalsResponse.lists[3].collaborators[0].userGuid", equalTo("2013348-2013348-2013348"));
        json.body("approvalsResponse.lists[3].collaborators[0].profilePicture", equalTo(""));
        json.body("approvalsResponse.lists[3].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("approvalsResponse.lists[3].collaborators[0].lastName", equalTo("User348"));
    }
}
