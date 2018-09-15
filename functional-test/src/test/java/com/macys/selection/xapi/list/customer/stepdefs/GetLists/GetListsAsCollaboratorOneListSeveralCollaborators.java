package com.macys.selection.xapi.list.customer.stepdefs.GetLists;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCustomerAllLists;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListsAsCollaboratorOneListSeveralCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who is collaborator of some list$")
    public void a_customer_with_userGuid_with_collaborative_list_as_collaborator(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer receives data for some list as collaborator$")
    public void a_customer_retrieves_colalborative_list_as_collaborator() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get lists data for customer who is collaborator of some list status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains data of collaborative list with several collaborators$")
    public void contains_collaborator_data_without_owner_list() {
        json.body("listsPresentation.lists.collaborativeList[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c10001008"));
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.collaborativeList[0].name", equalTo("Collaborator List 1"));
        json.body("listsPresentation.lists.collaborativeList[0].userGuid", equalTo("10002008-10002008-10002008"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].userGuid", equalTo("2013445-2013445-2013445"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].privilege", equalTo("LIKE"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].userGuid", equalTo("2013445-2013445-2013445"));

        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].userGuid", equalTo("2013446-2013446-2013446"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].privilege", equalTo("LIKE"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].userGuid", equalTo("2013446-2013446-2013446"));

        json.body("listsPresentation.lists.wishList[0].defaultList", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].searchable", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].id", equalTo(1000100701));
        json.body("listsPresentation.lists.wishList[0].listType", equalTo("W"));
        json.body("listsPresentation.lists.wishList[0].name", equalTo("Default list 7"));
        json.body("listsPresentation.lists.wishList[0].listGuid", equalTo("2e44b50c-1c11-4ed0-abe5-10001007"));
        json.body("listsPresentation.lists.wishList[0].numberOfItems", equalTo(0));
    }

}
