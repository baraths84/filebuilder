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
public class GetListsAsOwnerSeveralCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" which is collaborative list owner with several collaborators$")
    public void a_customer_with_userGuid_with_collaborative_list_as_owner_with_severalCollaborators(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer collaborative list with several collaborators where he is owner$")
    public void a_customer_retrieves_colalborative_list_with_severalCollaborators_data_as_owner() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list with first two collaborators status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding data of collaborators$")
    public void contains_all_list_typs_data_with_several_collaborators() {
        json.body("listsPresentation.lists.collaborativeList[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o10001004"));
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.collaborativeList[0].name", equalTo("Owner list 4"));
        json.body("listsPresentation.lists.collaborativeList[0].userGuid", equalTo("10001004-10001004-10001004"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].userGuid", equalTo("2013365-2013365-2013365"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].privilege", equalTo("EDIT"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].lastName", equalTo("User365"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[0].profilePicture", equalTo("avatar_image_1"));

        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].userGuid", equalTo("2013366-2013366-2013366"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].privilege", equalTo("LIKE"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].lastName", equalTo("User366"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[1].profilePicture", equalTo("avatar_image_2"));

        json.body("listsPresentation.lists.collaborativeList[0].collaborators[2].userGuid", equalTo("2013367-2013367-2013367"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[2].privilege", equalTo("COMMENT"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[2].lastName", equalTo("User367"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[2].profilePicture", equalTo("avatar_image_2"));

        json.body("listsPresentation.lists.collaborativeList[0].collaborators[3].userGuid", equalTo("2013368-2013368-2013368"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[3].privilege", equalTo("EDIT"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[3].lastName", equalTo("User368"));
        json.body("listsPresentation.lists.collaborativeList[0].collaborators[3].profilePicture", equalTo(null));

        json.body("listsPresentation.lists.wishList[0].defaultList", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].searchable", equalTo(Boolean.FALSE));
        json.body("listsPresentation.lists.wishList[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("listsPresentation.lists.wishList[0].listType", equalTo("W"));
        json.body("listsPresentation.lists.wishList[0].name", equalTo("Default list"));
        json.body("listsPresentation.lists.wishList[0].id", equalTo(1000100101));
        json.body("listsPresentation.lists.wishList[0].listGuid", equalTo("2e44b50c-1c11-4ed0-abe5-10001001"));
        json.body("listsPresentation.lists.wishList[0].numberOfItems", equalTo(0));
    }

}
