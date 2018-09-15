package com.macys.selection.xapi.list.customer.stepdefs.GetCollaborativeListDetails;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListDetailsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListDetailsWithItemsWithoutProduct {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\" without items without productId$")
    public void a_customer_with_collaborative_list(String listGuid, String userGuid) {
        request = given().pathParam("listGuid", listGuid).queryParams("userGuid", userGuid);
        System.out.println("Customer list request" + request);
    }

    @When("^owner gets list details with items without productId$")
    public void a_customer_retrieves_collaborative_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetCollaborativeListDetailsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list details with items without productId status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains list details with items without productId details$")
    public void contains_blank_listGuid() {
        json.body("errors.error[0].developerMessage", equalTo("Unable to find upc/product info from fcc."));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
