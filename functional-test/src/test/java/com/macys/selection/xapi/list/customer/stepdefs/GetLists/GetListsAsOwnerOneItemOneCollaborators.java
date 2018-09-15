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
public class GetListsAsOwnerOneItemOneCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" which is collaborative list owner$")
    public void a_customer_with_userGuid_with_collaborative_list_as_owner(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer collaborative list where he is owner$")
    public void a_customer_retrieves_colalborative_list_data_as_owner() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list as owner status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains data of owner list with item and collaborator$")
    public void contains_owner_and_collaborative_list_with__item_data() {
        json.body("listsPresentation.lists.collaborativeList[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001002"));
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.collaborativeList[0].name", equalTo("Owner list 2"));
        json.body("listsPresentation.lists.collaborativeList[0].userGuid", equalTo("10001002-10001002-10001002"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].privilege", equalTo("LIKE"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].firstName", equalTo("Collaborator"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].lastName", equalTo("User365"));


        json.body("listsPresentation.lists.wishList[0].defaultList", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].searchable", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].id", equalTo(1000100202));
        json.body("listsPresentation.lists.wishList[0].listType", equalTo("W"));
        json.body("listsPresentation.lists.wishList[0].name", equalTo("Default list 2"));
        json.body("listsPresentation.lists.wishList[0].numberOfItems", equalTo(0));
        json.body("listsPresentation.lists.wishList[0].listGuid", equalTo("2e44b50c-1c11-4ed0-abe5-10001002"));
        json.body("listsPresentation.lists.wishList[0].items[0].upc.id", equalTo(90001));
    }

}
