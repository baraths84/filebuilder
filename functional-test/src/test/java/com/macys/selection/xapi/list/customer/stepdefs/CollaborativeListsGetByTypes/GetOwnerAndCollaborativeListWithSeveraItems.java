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
public class GetOwnerAndCollaborativeListWithSeveraItems {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who is owner and collaborator with several items$")
    public void a_customer_with_userGuid_with_collaborative_and_owner_list(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets owner and collaborator list data with several items$")
    public void a_customer_retrieves_owner_and_colalborator_lists() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get customer owner and collaborator list  with several items status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding collaborator and owner list data with recent items$")
    public void contains_owner_and_collaborator_lists_with_items_data() {
        json.body("listsPresentation.lists.listOwner[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001002"));
        json.body("listsPresentation.lists.listOwner[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[0].name", equalTo("Owner list 2"));
        json.body("listsPresentation.lists.listOwner[0].userGuid", equalTo("10001002-10001002-10001002"));
        json.body("listsPresentation.lists.listOwner[0].items[0].upc.id", equalTo(90051));
        json.body("listsPresentation.lists.listOwner[0].items[1].upc.id", equalTo(90052));

        json.body("listsPresentation.lists.listCollaborator[0].userGuid", equalTo("10001020-10001020-10001020"));
        json.body("listsPresentation.lists.listCollaborator[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c10001012"));
        json.body("listsPresentation.lists.listCollaborator[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[0].name", equalTo("Collaborator List 1"));
        json.body("listsPresentation.lists.listCollaborator[0].items[0].upc.id", equalTo(90061));
        json.body("listsPresentation.lists.listCollaborator[0].items[1].upc.id", equalTo(90062));
    }
}
