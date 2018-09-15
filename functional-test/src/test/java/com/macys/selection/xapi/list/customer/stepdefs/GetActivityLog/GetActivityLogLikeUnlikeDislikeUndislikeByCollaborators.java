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
public class GetActivityLogLikeUnlikeDislikeUndislikeByCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" wants to get activity log of list \"([^\"]*)\" with like, unlike, dislike, undislike activities$")
    public void list_owner_with_userGuid_and_listGuid_of_list_with_add_remove_items_activities(String userGuid, String listGuid) {
        request = given().queryParam("userGuid", userGuid).pathParams("listGuid", listGuid);
        System.out.println("Get activity log request :" + request);
    }

    @When("^owner gets like, unlike, dislike, undislike activities in activity log$")
    public void list_owner_receives_list_activity_log() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListActivityLog());
        System.out.println("Customer list activities :" + response.prettyPrint());
    }

       /* New Flow calls:
        1)Check collaborator profile in customer:
          /api/customer/v1/profile/guestUser?userGuid=99901
        2)Get activity log:
          /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99990/activitylog?userGuid=99901&guestUser=false
        3) Get productId of items by upcId from fcc:
          /api/catalog/v2/upcs/90001,90002?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        4) Get product name from fcc:
          /api/catalog/v2/products/9000101,9000202?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds%2CfinalPrice&active=true&includeFinalPrice=true
        5) Get customer info (First/Last name) of collaborators from customer:
          /api/customer/v2/profile/list?userGuids=99920%2C99921
        */

    @Then("^get like, unlike, dislike, undislike activity log status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains like, unlike, dislike, undislike activities data$")
    public void contains_list_like_unlike_dislike_undislike_activities() {
        json.body("activityLogPage.activityLog[0].listGuid", equalTo("dfa1ece-db69-4369-a222-99990"));
        json.body("activityLogPage.activityLog[0].userGuid", equalTo("99920-99920"));
        json.body("activityLogPage.activityLog[0].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[0].userLastName", equalTo("User20"));
        json.body("activityLogPage.activityLog[0].productName", equalTo("OXO Meat Tenderizer"));
        json.body("activityLogPage.activityLog[0].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[0].profilePictureUrl", equalTo("avatar image 1"));

        json.body("activityLogPage.activityLog[1].listGuid", equalTo("dfa1ece-db69-4369-a222-99990"));
        json.body("activityLogPage.activityLog[1].userGuid", equalTo("99920-99920"));
        json.body("activityLogPage.activityLog[1].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[1].userLastName", equalTo("User20"));
        json.body("activityLogPage.activityLog[1].productName", equalTo("OXO Meat Tenderizer"));
        json.body("activityLogPage.activityLog[1].activityType", equalTo("UNLIKE"));
        json.body("activityLogPage.activityLog[1].profilePictureUrl", equalTo("avatar image 1"));

        json.body("activityLogPage.activityLog[2].listGuid", equalTo("dfa1ece-db69-4369-a222-99990"));
        json.body("activityLogPage.activityLog[2].userGuid", equalTo("99921-99921"));
        json.body("activityLogPage.activityLog[2].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[2].userLastName", equalTo("User21"));
        json.body("activityLogPage.activityLog[2].productName", equalTo("OXO Can Opener"));
        json.body("activityLogPage.activityLog[2].activityType", equalTo("DISLIKE"));
        json.body("activityLogPage.activityLog[2].profilePictureUrl", equalTo("avatar image 2"));

        json.body("activityLogPage.activityLog[3].listGuid", equalTo("dfa1ece-db69-4369-a222-99990"));
        json.body("activityLogPage.activityLog[3].userGuid", equalTo("99921-99921"));
        json.body("activityLogPage.activityLog[3].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[3].userLastName", equalTo("User21"));
        json.body("activityLogPage.activityLog[3].productName", equalTo("OXO Can Opener"));
        json.body("activityLogPage.activityLog[3].activityType", equalTo("UNDISLIKE"));
        json.body("activityLogPage.activityLog[3].profilePictureUrl", equalTo("avatar image 2"));
    }
}
