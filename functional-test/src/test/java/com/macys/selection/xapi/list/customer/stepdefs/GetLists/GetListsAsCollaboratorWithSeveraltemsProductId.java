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
public class GetListsAsCollaboratorWithSeveraltemsProductId {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" which is collaborator of list with several items by productId$")
    public void a_customer_with_userGuid_with_collaborative_list_with_severalItems_productId(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets collaborative list with several items with productId$")
    public void a_customer_retrieves_colalborative_list_with_severalItems_with_productId() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list with several items with productId status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains data of first 3 items with productId for collaborator$")
    public void contains_owner_with_collaborator_and_wishlist_data() {
        json.body("listsPresentation.lists.collaborativeList[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001010"));
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.collaborativeList[0].name", equalTo("Owner list 3"));
        json.body("listsPresentation.lists.collaborativeList[0].numberOfItems", equalTo(4));
        json.body("listsPresentation.lists.collaborativeList[0].userGuid", equalTo("10001010-10001010-10001010"));
        json.body("listsPresentation.lists.collaborativeList[0].items[0].product.id", equalTo(9006101));
        json.body("listsPresentation.lists.collaborativeList[0].items[0].qtyRequested", equalTo(1));
        json.body("listsPresentation.lists.collaborativeList[0].items[1].product.id", equalTo(9006202));
        json.body("listsPresentation.lists.collaborativeList[0].items[1].qtyRequested", equalTo(1));
        json.body("listsPresentation.lists.collaborativeList[0].items[2].product.id", equalTo(9006302));
        json.body("listsPresentation.lists.collaborativeList[0].items[2].qtyRequested", equalTo(1));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators", equalTo(null));

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
