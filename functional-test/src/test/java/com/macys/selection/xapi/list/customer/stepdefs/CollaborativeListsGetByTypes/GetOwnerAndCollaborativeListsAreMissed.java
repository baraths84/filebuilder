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
public class GetOwnerAndCollaborativeListsAreMissed {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^user with userGuid \"([^\"]*)\" who didn't own any collaborative list and isn't a collaborator$")
    public void a_user_with_userGuid_without_collaborative_and_owner_list_(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^user gets owner and collaborative lists data$")
    public void a_customer_retrieves_owner_and_colalborator_blank_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get owner and collaborative blank lists data status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding blank lists data$")
    public void contains_owner_and_collaborator_blank_lists_data() {
        json.body("listsPresentation.lists.listOwner[0]", equalTo(null));
        json.body("listsPresentation.lists.listCollaborator[0]", equalTo(null));
    }
}
