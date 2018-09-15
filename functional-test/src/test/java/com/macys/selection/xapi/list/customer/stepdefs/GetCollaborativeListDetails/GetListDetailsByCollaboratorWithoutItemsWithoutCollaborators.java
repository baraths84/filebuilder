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
public class GetListDetailsByCollaboratorWithoutItemsWithoutCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\" without items and without collaborators$")
    public void a_customer_with_collaborative_list(String listGuid, String userGuid) {
        request = given().pathParam("listGuid", listGuid).queryParams("userGuid", userGuid);
        System.out.println("Customer list request" + request);
    }

    @When("^owner gets list details without items and without collaborators$")
    public void a_customer_retrieves_collaborative_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetCollaborativeListDetailsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list details without items and without collaborators status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains list without items and without collaborators details$")
    public void contains_blank_listGuid() {
        json.body("collaborativeListDetails.viewer.guid", equalTo("10001001-10001001-10001001"));
        json.body("collaborativeListDetails.viewer.firstName", equalTo("FirstName"));
        json.body("collaborativeListDetails.viewer.lastName", equalTo("User 1001"));
        json.body("collaborativeListDetails.viewer.profilePicture", equalTo("avatar image 1"));

        json.body("collaborativeListDetails.owner.guid", equalTo("10001001-10001001-10001001"));
        json.body("collaborativeListDetails.owner.firstName", equalTo("Collaborator"));
        json.body("collaborativeListDetails.owner.lastName", equalTo("User 1001"));
        json.body("collaborativeListDetails.owner.profilePicture", equalTo("avatar image 1"));

        json.body("collaborativeListDetails.listGuid", equalTo("hfs9df9-asd9v0-c359-4de9-b083-987-10001004"));
        json.body("collaborativeListDetails.name", equalTo("List without collaborators and with items"));
        json.body("collaborativeListDetails.numberOfItems", equalTo(0));
        json.body("collaborativeListDetails.numberOfCollaborators", equalTo(0));

        json.body("collaborativeListDetails.collaborators[0]", equalTo(null));
        json.body("collaborativeListDetails.items[0]", equalTo(null));
    }


}
