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
public class GetOwnerAndCollaborativeListWithOneCollaborator {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who is owner and collaborator with collaborator in both lists$")
    public void a_customer_with_userGuid_with_collaborative_and_owner_list_with_collaborator(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets owner and collaborator list data with collaborator$")
    public void a_customer_retrieves_owner_and_colalborator_lists_with_collaborator() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get customer owner and collaborator list with collaborator status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding collaborator and owner list data with collaborator$")
    public void contains_owner_and_collaborator_lists_with_collaborator_data() {
        json.body("listsPresentation.lists.listOwner[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001020"));
        json.body("listsPresentation.lists.listOwner[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[0].name", equalTo("Owner list 2"));
        json.body("listsPresentation.lists.listOwner[0].userId", equalTo(10001020));
        json.body("listsPresentation.lists.listOwner[0].collaborators[0].userId", equalTo(2013365));

        json.body("listsPresentation.lists.listCollaborator[0].userId", equalTo(10001020));
        json.body("listsPresentation.lists.listCollaborator[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c10001020"));
        json.body("listsPresentation.lists.listCollaborator[0].listType", equalTo("W"));
        json.body("listsPresentation.lists.listCollaborator[0].name", equalTo("Collaborator List 1"));
        json.body("listsPresentation.lists.listCollaborator[0].numberOfItems", equalTo(2));
    }
}
