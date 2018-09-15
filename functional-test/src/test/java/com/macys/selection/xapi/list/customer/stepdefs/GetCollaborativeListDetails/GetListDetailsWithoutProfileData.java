package com.macys.selection.xapi.list.customer.stepdefs.GetCollaborativeListDetails;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListDetailsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListDetailsWithoutProfileData {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with listGuid \"([^\"]*)\" without collaborators profile data and userGuid \"([^\"]*)\"$")
    public void a_customer_with_collaborative_list(String listGuid, String userGuid) {
        request = given().pathParam("listGuid", listGuid).queryParams("userGuid", userGuid);
        System.out.println("Customer list request" + request);
    }

    @When("^owner gets list details without collaborators profile data$")
    public void a_customer_retrieves_collaborative_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetCollaborativeListDetailsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list details without collaborators profile data status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains list details without collaborators profile data$")
    public void contains_blank_listGuid() {
        json.body("collaborativeListDetails.viewer.guid", equalTo("10001222-10001222-10001222"));
        json.body("collaborativeListDetails.viewer.firstName", equalTo(null));
        json.body("collaborativeListDetails.viewer.lastName", equalTo(null));
        json.body("collaborativeListDetails.viewer.profilePicture", equalTo(null));

        json.body("collaborativeListDetails.owner.guid", equalTo("10001222-10001222-10001222"));
        json.body("collaborativeListDetails.owner.firstName", equalTo(null));
        json.body("collaborativeListDetails.owner.lastName", equalTo(null));
        json.body("collaborativeListDetails.owner.profilePicture", equalTo(null));

        json.body("collaborativeListDetails.listGuid", equalTo("hfs9df9-asd9v0-c359-4de9-b083-987-10001008"));
        json.body("collaborativeListDetails.name", equalTo("List without collaborators profile data"));
        json.body("collaborativeListDetails.numberOfItems", equalTo(0));
        json.body("collaborativeListDetails.numberOfCollaborators", equalTo(2));

        json.body("collaborativeListDetails.collaborators[0].userGuid", equalTo("2150901-2150901-2150901"));
        json.body("collaborativeListDetails.collaborators[0].profilePicture", equalTo(""));
        json.body("collaborativeListDetails.collaborators[0].privilege", equalTo("LIKE"));
        json.body("collaborativeListDetails.collaborators[0].firstName", equalTo(null));
        json.body("collaborativeListDetails.collaborators[0].lastName", equalTo(null));

        json.body("collaborativeListDetails.collaborators[1].userGuid", equalTo("2150902-2150902-2150902"));
        json.body("collaborativeListDetails.collaborators[1].profilePicture", equalTo(""));
        json.body("collaborativeListDetails.collaborators[1].lastName", equalTo(null));
        json.body("collaborativeListDetails.collaborators[1].lastName", equalTo(null));
        json.body("collaborativeListDetails.collaborators[1].privilege", equalTo("LIKE"));
    }
}
