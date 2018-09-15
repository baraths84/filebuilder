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
public class GetActivityLogSingleItemSeveralCollaborators {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^list owner with userGuid \"([^\"]*)\" wants to get activity log of list \"([^\"]*)\" with activity from several collaborators about one item")
    public void list_owner_with_userGuid_and_listGuid_of_list_with_several_collaborators(String userGuid, String listGuid) {
        request = given().queryParam("userGuid", userGuid).pathParams("listGuid", listGuid);
        System.out.println("Get activity log request :" + request);
    }

    @When("^owner gets activity log with one item and several collaborators$")
    public void list_owner_receives_list_with_activity_log_of_several_collaborators() {
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
          /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99996/activitylog?userGuid=99901&guestUser=false
        3) Get productId of items by upcId from fcc:
          /api/catalog/v2/upcs/90001?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        4) Get product name from fcc:
          /api/catalog/v2/products/9000101?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds%2CfinalPrice&active=true&includeFinalPrice=true
        5) Get customer info (First/Last name) of collaborators from customer:
          /api/customer/v2/profile/list?userGuids=99931%2C99932%2C99933%2C99934%2C99935%2C99936%2C99937%2C99938
        */

    @Then("^get activity log with one item and several collaborators status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains activity of several collaborators about one item data$")
    public void contains_list_dislike_activity_data() {
        json.body("activityLogPage.activityLog[0].listGuid", equalTo("dfa1ece-db69-4369-a222-99996"));
        json.body("activityLogPage.activityLog[0].userGuid", equalTo("99931-99931"));
        json.body("activityLogPage.activityLog[0].userFirstName", equalTo("Collaborator"));
        json.body("activityLogPage.activityLog[0].userLastName", equalTo("User31"));
        json.body("activityLogPage.activityLog[0].productName", equalTo("American Rag Men's Thermal Long Sleeve Shirt, Created for Macy's"));
        json.body("activityLogPage.activityLog[0].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[0].profilePictureUrl", equalTo("avatar image 1"));

        json.body("activityLogPage.activityLog[1].userGuid", equalTo("99932-99932"));
        json.body("activityLogPage.activityLog[1].userLastName", equalTo("User32"));
        json.body("activityLogPage.activityLog[1].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[1].profilePictureUrl", equalTo("avatar image 2"));

        json.body("activityLogPage.activityLog[2].userGuid", equalTo("99933-99933"));
        json.body("activityLogPage.activityLog[2].userLastName", equalTo("User33"));
        json.body("activityLogPage.activityLog[2].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[2].profilePictureUrl", equalTo("avatar image 3"));

        json.body("activityLogPage.activityLog[3].userGuid", equalTo("99934-99934"));
        json.body("activityLogPage.activityLog[3].userLastName", equalTo("User34"));
        json.body("activityLogPage.activityLog[3].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[3].profilePictureUrl", equalTo(null));

        json.body("activityLogPage.activityLog[4].userGuid", equalTo("99935-99935"));
        json.body("activityLogPage.activityLog[4].userLastName", equalTo("User35"));
        json.body("activityLogPage.activityLog[4].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[4].profilePictureUrl", equalTo(""));

        json.body("activityLogPage.activityLog[5].userGuid", equalTo("99936-99936"));
        json.body("activityLogPage.activityLog[5].userLastName", equalTo("User36"));
        json.body("activityLogPage.activityLog[5].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[5].profilePictureUrl", equalTo("avatar image 2 avatar image 2 avatar image 2"));

        json.body("activityLogPage.activityLog[6].userGuid", equalTo("99931-99931"));
        json.body("activityLogPage.activityLog[6].userLastName", equalTo("User31"));
        json.body("activityLogPage.activityLog[6].activityType", equalTo("UNLIKE"));
        json.body("activityLogPage.activityLog[6].profilePictureUrl", equalTo("avatar image 1"));

        json.body("activityLogPage.activityLog[7].userGuid", equalTo("99931-99931"));
        json.body("activityLogPage.activityLog[7].userLastName", equalTo("User31"));
        json.body("activityLogPage.activityLog[7].activityType", equalTo("DISLIKE"));
        json.body("activityLogPage.activityLog[7].profilePictureUrl", equalTo("avatar image 1"));

        json.body("activityLogPage.activityLog[8].userGuid", equalTo("99937-99937"));
        json.body("activityLogPage.activityLog[8].userLastName", equalTo("User37"));
        json.body("activityLogPage.activityLog[8].activityType", equalTo("DISLIKE"));
        json.body("activityLogPage.activityLog[8].profilePictureUrl", equalTo("avatar image #1!"));

        json.body("activityLogPage.activityLog[9].userGuid", equalTo("99937-99937"));
        json.body("activityLogPage.activityLog[9].userLastName", equalTo("User37"));
        json.body("activityLogPage.activityLog[9].activityType", equalTo("UNDISLIKE"));
        json.body("activityLogPage.activityLog[9].profilePictureUrl", equalTo("avatar image #1!"));

        json.body("activityLogPage.activityLog[10].userGuid", equalTo("99938-99938"));
        json.body("activityLogPage.activityLog[10].userLastName", equalTo("User38"));
        json.body("activityLogPage.activityLog[10].activityType", equalTo("DISLIKE"));
        json.body("activityLogPage.activityLog[10].profilePictureUrl", equalTo("avatar image 8"));

        json.body("activityLogPage.activityLog[11].userGuid", equalTo("99936-99936"));
        json.body("activityLogPage.activityLog[11].userLastName", equalTo("User36"));
        json.body("activityLogPage.activityLog[11].activityType", equalTo("UNLIKE"));
        json.body("activityLogPage.activityLog[11].profilePictureUrl", equalTo("avatar image 2 avatar image 2 avatar image 2"));

        json.body("activityLogPage.activityLog[12].userGuid", equalTo("99937-99937"));
        json.body("activityLogPage.activityLog[12].userLastName", equalTo("User37"));
        json.body("activityLogPage.activityLog[12].activityType", equalTo("LIKE"));
        json.body("activityLogPage.activityLog[12].profilePictureUrl", equalTo("avatar image #1!"));
    }
}
