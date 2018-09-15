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
public class OwnerGetCollaboratorsDataByIncorrectUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner user with incorrect userGuid \"([^\"]*)\"$")
    public void owner_user_with_several_lists_with_collaborators(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^owner gets collaborators data by incorrect userGuid$")
    public void owner_retrives_several_collaborators_from_several_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetAllCollaboratorsFromAllListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborators data by incorrect userGuid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains incorrect owner userGuid error$")
    public void contains_all_list_types_data() {
        json.body("errors.error[0].developerMessage", equalTo("Invalid User ID."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
