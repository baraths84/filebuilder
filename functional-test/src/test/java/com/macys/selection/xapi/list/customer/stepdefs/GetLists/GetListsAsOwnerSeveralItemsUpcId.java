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
public class GetListsAsOwnerSeveralItemsUpcId {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" which is collaborative list owner with several items$")
    public void a_customer_with_userGuid_with_collaborative_list_as_owner_with_severalItems(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer collaborative list with several items where he is owner$")
    public void a_customer_retrieves_colalborative_list_with_severalItems_data_as_owner() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list with several items as owner status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains data of first 3 items for owner$")
    public void contains_owner_with_collaborator_and_wishlist_data() {
        json.body("listsPresentation.lists.collaborativeList[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001002"));
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.collaborativeList[0].name", equalTo("Owner list 2"));
        json.body("listsPresentation.lists.collaborativeList[0].numberOfItems", equalTo(4));
        json.body("listsPresentation.lists.collaborativeList[0].userGuid", equalTo("10001002-10001002-10001002"));
        json.body("listsPresentation.lists.collaborativeList[0].items[0].upc.id", equalTo(90051));
        json.body("listsPresentation.lists.collaborativeList[0].items[1].upc.id", equalTo(90052));
        json.body("listsPresentation.lists.collaborativeList[0].items[2].upc.id", equalTo(90053));

        json.body("listsPresentation.lists.wishList[0].defaultList", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].searchable", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].listType", equalTo("W"));
        json.body("listsPresentation.lists.wishList[0].name", equalTo("Default list"));
        json.body("listsPresentation.lists.wishList[0].numberOfItems", equalTo(0));
        json.body("listsPresentation.lists.wishList[0].id", equalTo(1000100101));
        json.body("listsPresentation.lists.wishList[0].listGuid", equalTo("2e44b50c-1c11-4ed0-abe5-10001001"));
    }

}
