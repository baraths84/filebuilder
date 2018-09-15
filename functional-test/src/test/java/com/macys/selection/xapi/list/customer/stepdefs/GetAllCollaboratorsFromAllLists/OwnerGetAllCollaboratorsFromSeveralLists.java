package com.macys.selection.xapi.list.customer.stepdefs.GetAllCollaboratorsFromAllLists;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCustomerAllLists;
import static com.macys.selection.xapi.list.util.TestUtils.getGetAllCollaboratorsFromAllListsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class OwnerGetAllCollaboratorsFromSeveralLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with userGuid \"([^\"]*)\" who has several lists with collaborators$")
    public void owner_user_with_several_lists_with_collaborators(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner user gets all collaborators from several lists$")
    public void owner_retrives_several_collaborators_from_several_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetAllCollaboratorsFromAllListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborators from several lists status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains data about collaborators from several lists$")
    public void contains_all_list_types_data() {
        json.body("CollaborativeListResponse.collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("CollaborativeListResponse.collaborators[0].lastName", equalTo("User365"));
        json.body("CollaborativeListResponse.collaborators[0].profilePicture", equalTo("avatar_image_1"));

        json.body("CollaborativeListResponse.collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("CollaborativeListResponse.collaborators[1].lastName", equalTo("User366"));
        json.body("CollaborativeListResponse.collaborators[1].profilePicture", equalTo("avatar_image_2"));

        json.body("CollaborativeListResponse.collaborators[2].userGuid", equalTo("2013367-2013367-2013367"));
        json.body("CollaborativeListResponse.collaborators[2].lastName", equalTo("User367"));
        json.body("CollaborativeListResponse.collaborators[2].profilePicture", equalTo("avatar_image_2"));

        json.body("CollaborativeListResponse.collaborators[3].userGuid", equalTo("2013368-2013368-2013368"));
        json.body("CollaborativeListResponse.collaborators[3].lastName", equalTo("User368"));
        json.body("CollaborativeListResponse.collaborators[3].profilePicture", equalTo(null));
    }
}
