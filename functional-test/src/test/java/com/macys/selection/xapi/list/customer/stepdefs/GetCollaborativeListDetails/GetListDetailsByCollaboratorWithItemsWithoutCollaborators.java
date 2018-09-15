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
public class GetListDetailsByCollaboratorWithItemsWithoutCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^owner with listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\" with items and without collaborators$")
    public void a_customer_with_collaborative_list(String listGuid, String userGuid) {
        request = given().pathParam("listGuid", listGuid).queryParams("userGuid", userGuid);
        System.out.println("Customer list request" + request);
    }

    @When("^owner gets list details with items and without collaborators$")
    public void a_customer_retrieves_collaborative_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getGetCollaborativeListDetailsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative list details with items and without collaborators status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains list with items and without collaborators details$")
    public void contains_blank_listGuid() {
        json.body("collaborativeListDetails.viewer.guid", equalTo("10001001-10001001-10001001"));
        json.body("collaborativeListDetails.viewer.firstName", equalTo("FirstName"));
        json.body("collaborativeListDetails.viewer.lastName", equalTo("User 1001"));
        json.body("collaborativeListDetails.viewer.profilePicture", equalTo("avatar image 1"));

        json.body("collaborativeListDetails.owner.guid", equalTo("10001001-10001001-10001001"));
        json.body("collaborativeListDetails.owner.firstName", equalTo("Collaborator"));
        json.body("collaborativeListDetails.owner.lastName", equalTo("User 1001"));
        json.body("collaborativeListDetails.owner.profilePicture", equalTo("avatar image 1"));

        json.body("collaborativeListDetails.listGuid", equalTo("hfs9df9-asd9v0-c359-4de9-b083-987-10001003"));
        json.body("collaborativeListDetails.name", equalTo("List without collaborators but with items"));
        json.body("collaborativeListDetails.numberOfItems", equalTo(2));
        json.body("collaborativeListDetails.numberOfCollaborators", equalTo(0));

        json.body("collaborativeListDetails.items[0].itemGuid", equalTo("0cba5e7f-3715-403f-a8ff-9000301"));
        json.body("collaborativeListDetails.items[0].id", equalTo(9000301));
        json.body("collaborativeListDetails.items[0].priority", equalTo("H"));
        json.body("collaborativeListDetails.items[0].upc.id", equalTo(90003));
        json.body("collaborativeListDetails.items[0].upc.product.id", equalTo(9000303));
        json.body("collaborativeListDetails.items[0].upc.product.name", equalTo("OXO Meat Tenderizer"));

        json.body("collaborativeListDetails.items[0].likes", equalTo(0));
        json.body("collaborativeListDetails.items[0].dislikes", equalTo(0));

        json.body("collaborativeListDetails.items[1].id", equalTo(9000401));
        json.body("collaborativeListDetails.items[1].upc.id", equalTo(90004));
        json.body("collaborativeListDetails.items[1].upc.product.id", equalTo(9000404));
        json.body("collaborativeListDetails.items[1].upc.product.name", equalTo("OXO Can Opener"));

        json.body("collaborativeListDetails.items[1].likes", equalTo(0));
        json.body("collaborativeListDetails.items[1].dislikes", equalTo(0));

        json.body("collaborativeListDetails.collaborators[0]", equalTo( null));

        json.body("collaborativeListDetails.recentActivity.listGuid", equalTo( "hfs9df9-asd9v0-c359-4de9-b083-987-10001003"));
        json.body("collaborativeListDetails.recentActivity.userGuid", equalTo( "10001001-10001001-10001001"));
        json.body("collaborativeListDetails.recentActivity.userFirstName", equalTo( "Collaborator"));
        json.body("collaborativeListDetails.recentActivity.userLastName", equalTo( "User 1001"));
        json.body("collaborativeListDetails.recentActivity.profilePictureUrl", equalTo( "avatar image 1"));
        json.body("collaborativeListDetails.recentActivity.productName", equalTo( "American Rag Men's Thermal Long Sleeve Shirt, Created for Macy's"));
        json.body("collaborativeListDetails.recentActivity.activityType", equalTo( "LIKE"));
    }


}
