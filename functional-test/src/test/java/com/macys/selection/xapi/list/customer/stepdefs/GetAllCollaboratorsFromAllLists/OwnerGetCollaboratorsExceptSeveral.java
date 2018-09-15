package com.macys.selection.xapi.list.customer.stepdefs.GetAllCollaboratorsFromAllLists;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetAllCollaboratorsFromAllListsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class OwnerGetCollaboratorsExceptSeveral {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with userGuid \"([^\"]*)\" with list with several collaborators$")
    public void user_with_list_with_several_collaborators(String userGuid) {
        request = given().queryParams("userGuid", userGuid, "exclude", "hrn6kdn-100010018, hrn6kdn-100010019, hrn6kdn-100010020");
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets collaborators data except several from lists$")
    public void owner_retrives_collaborators_except_several_from_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetAllCollaboratorsFromAllListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborators data except several from lists status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains collaborators data except several$")
    public void contains_all_list_types_data() {
        json.body("CollaborativeListResponse.collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("CollaborativeListResponse.collaborators[0].lastName", equalTo("User365"));
        json.body("CollaborativeListResponse.collaborators[0].profilePicture", equalTo("avatar_image_1"));

        json.body("CollaborativeListResponse.collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("CollaborativeListResponse.collaborators[1].lastName", equalTo("User366"));
        json.body("CollaborativeListResponse.collaborators[1].profilePicture", equalTo("avatar_image_2"));
    }
}
