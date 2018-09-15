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
public class GetListsAsOwnerNoLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who doesn't have any lists$")
    public void a_user_with_userGuid_with_blank_owner_and_default_list(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets collaborative blank response$")
    public void a_customer_retrieves_blank_colalborative_list_data_as_owner() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCustomerAllLists());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative blank response status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains blank response list for owner$")
    public void contains_blank_lists_data() {
        json.body("listsPresentation.lists.collaborativeList[0].listType", equalTo(null));
        json.body("listsPresentation.lists.wishList[0].listType", equalTo(null));
    }

}
