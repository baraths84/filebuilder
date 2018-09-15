package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListEditRemove;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListByGuidEndpoint;
import static io.restassured.RestAssured.given;


/**
 * Step definition file for get list by list guid.
 **/
public class DeleteCollaborativeListByUserIdWithoutUserGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^collaborative list with listGuid \"([^\"]*)\" and userId \"([^\"]*)\" without userGuid$")
    public void a_customer_with_collaborative_listguid_and_userId_without_userGuid(String listGuid, String userId) {
        request = given().pathParam("listguid", listGuid).queryParam("userId", userId);
        System.out.println("Delete Collaborate list request" + request);
    }

    @When("^customer tries to delete collaborative list by listGuid and userId without userGuid$")
    public void user_delete_collaborative_list_by_userId_without_userGuid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getGetCollaborativeListByGuidEndpoint());
        System.out.println("Delete Collaborate list response" + response.prettyPrint());
    }

    @Then("^the delete collaborative list status code with userId without userGuid is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

}