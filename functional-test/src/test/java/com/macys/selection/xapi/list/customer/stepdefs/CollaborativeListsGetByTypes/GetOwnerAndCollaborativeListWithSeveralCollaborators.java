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
public class GetOwnerAndCollaborativeListWithSeveralCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who is owner and collaborator with several collaborators$")
    public void a_customer_with_userGuid_with_collaborative_and_owner_list_with_several_collaborators(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets owner and collaborator list data with several collaborators$")
    public void a_customer_retrieves_owner_and_colalborator_lists_with_several_collaborators() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get customer owner and collaborator list  with several collaborators status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding collaborator and owner list data with collaborators$")
    public void contains_owner_and_collaborator_lists_with_collaboratos_data() {
        json.body("listsPresentation.lists.listOwner[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001004"));
        json.body("listsPresentation.lists.listOwner[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[0].name", equalTo("Owner list 4"));
        json.body("listsPresentation.lists.listOwner[0].userGuid", equalTo("10001004-10001004-10001004"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[0].profilePicture", equalTo("avatar_image_1"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[0].lastName", equalTo("User365"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[1].lastName", equalTo("User366"));
        json.body("listsPresentation.lists.listOwner[0].collaborators[1].profilePicture", equalTo("avatar_image_2"));

        json.body("listsPresentation.lists.listCollaborator[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c10001004"));
        json.body("listsPresentation.lists.listCollaborator[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[0].name", equalTo("Collaborator list 4"));
        json.body("listsPresentation.lists.listCollaborator[0].numberOfItems", equalTo(2));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[0].privilege", equalTo("LIKE"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[0].profilePicture", equalTo("avatar_image_1"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[0].lastName", equalTo("User365"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[1].privilege", equalTo("EDIT"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[1].profilePicture", equalTo("avatar_image_2"));
        json.body("listsPresentation.lists.listCollaborator[0].collaborators[1].lastName", equalTo("User366"));
    }
}
