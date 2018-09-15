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
public class OwnerGetAllCollaboratorsWithoutProfileData {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner user with userGuid \"([^\"]*)\" and collaborators without profile data$")
    public void owner_user_with_one_lists_with_collaborators_without_profile_data(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets collaborators without profile data by userGuid$")
    public void owner_retrives_collaborators_without_profile_data_from_one_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetAllCollaboratorsFromAllListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborators without profile data by userGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains collaborators without profile data$")
    public void contains_all_list_types_data() {
        json.body("CollaborativeListResponse.collaborators[0].userGuid", equalTo("99989-99989"));
        json.body("CollaborativeListResponse.collaborators[0].lastName", equalTo(null));
        json.body("CollaborativeListResponse.collaborators[0].profilePicture", equalTo("avatar image 89"));

        json.body("CollaborativeListResponse.collaborators[1].userGuid", equalTo("99988-99988"));
        json.body("CollaborativeListResponse.collaborators[1].lastName", equalTo(""));
        json.body("CollaborativeListResponse.collaborators[1].profilePicture", equalTo("avatar image 88"));
    }
}
