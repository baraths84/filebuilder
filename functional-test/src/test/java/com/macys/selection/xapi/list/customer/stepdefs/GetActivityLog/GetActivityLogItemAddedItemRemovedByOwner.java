package com.macys.selection.xapi.list.customer.stepdefs.GetActivityLog;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListActivityLog;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetActivityLogItemAddedItemRemovedByOwner {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" wants to get activity log of list \"([^\"]*)\" with item added and item removed activities$")
    public void list_owner_with_userId_and_listGuid_of_list_with_activities(String userGuid, String listGuid) {
        request = given().queryParam("userGuid", userGuid).pathParams("listGuid", listGuid);
        System.out.println("Get activity log request :" + request);
    }

    @When("^owner gets item added and item removed activities in activity log$")
    public void list_owner_receives_list_activity_log() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListActivityLog());
        System.out.println("Customer list activities :" + response.prettyPrint());
    }

    /* New Flow calls:
        1)Check collaborator profile in customer:
          /api/customer/v1/profile/guestUser?userid=99901
        2)Get activity log:
          /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99991/activitylog?userId=99901&guestUser=false
        3) Get productId of items by upcId from fcc:
          /api/catalog/v2/upcs/81002?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        4) Get product name from fcc:
          /api/catalog/v2/products/8100201?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds%2CfinalPrice&active=true&includeFinalPrice=true
        5) Get customer info (First/Last name) of collaborators from customer:
          /api/customer/v2/profile/list?userIds=99901
        */

    @Then("^get item added and item removed activity log status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains item added and item removed activities data$")
    public void contains_add_remove_item_activities() {
        json.body("activityLogPage.activityLog[0].listGuid", equalTo("dfa1ece-db69-4369-a222-99991"));
        json.body("activityLogPage.activityLog[0].userGuid", equalTo("99901-99901"));
        json.body("activityLogPage.activityLog[0].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[0].userLastName", equalTo("User1"));
        json.body("activityLogPage.activityLog[0].productName", equalTo("Calvin Klein Illusion Bandage Dress"));
        json.body("activityLogPage.activityLog[0].activityType", equalTo("ADD_ITEM"));
        json.body("activityLogPage.activityLog[0].profilePictureUrl", equalTo("avatar image 2"));

        json.body("activityLogPage.activityLog[1].listGuid", equalTo("dfa1ece-db69-4369-a222-99991"));
        json.body("activityLogPage.activityLog[1].userGuid", equalTo("99901-99901"));
        json.body("activityLogPage.activityLog[1].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[1].userLastName", equalTo("User1"));
        json.body("activityLogPage.activityLog[1].productName", equalTo("Calvin Klein Illusion Bandage Dress"));
        json.body("activityLogPage.activityLog[1].activityType", equalTo("REMOVE_ITEM"));
        json.body("activityLogPage.activityLog[1].profilePictureUrl", equalTo("avatar image 2"));
    }
}
