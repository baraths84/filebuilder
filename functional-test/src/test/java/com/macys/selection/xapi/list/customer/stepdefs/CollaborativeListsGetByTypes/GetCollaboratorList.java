package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListsGetByTypes;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCollaborativeListsByTypes;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetCollaboratorList {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;


    @Given("^user with userGuid \"([^\"]*)\" who is collaborator of some list$")
    public void a_customer_with_userGuid_with_collaborative_list(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^user gets collaborator list data$")
    public void a_customer_retrieves_colalborator_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborator list data status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding collaborator list data$")
    public void contains_collaborator_list_data() {
        json.body("listsPresentation.lists.listOwner[0]", equalTo(null));

        json.body("listsPresentation.lists.listCollaborator[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001016"));
        json.body("listsPresentation.lists.listCollaborator[0].userGuid", equalTo("10002007-10002007-10002007"));
        json.body("listsPresentation.lists.listCollaborator[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[0].name", equalTo("Collaborative list 5"));
        json.body("listsPresentation.lists.listCollaborator[0].numberOfItems", equalTo(2));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[0]", equalTo(null));
    }
}
