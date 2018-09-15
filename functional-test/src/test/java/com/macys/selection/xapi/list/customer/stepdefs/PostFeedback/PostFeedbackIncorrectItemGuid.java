package com.macys.selection.xapi.list.customer.stepdefs.PostFeedback;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.macys.selection.xapi.list.util.TestUtils.addCollaboratorFeedback;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostFeedbackIncorrectItemGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostFeedbackIncorrectItemGuid.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^collaborator with userGuid \"([^\"]*)\" post like feedback for incorrect itemGuid \"([^\"]*)\" in the collaborative listGuid \"([^\"]*)\"$")
    public void collaborator_with_listGuid_and_incorrect_itemGuid(String userGuid, String itemGuid, String listGuid) {
        request = given().pathParams("itemGuid", itemGuid, "listGuid", listGuid).queryParams("userGuid", userGuid, "itemFeedback", "liked");
    }

    @When("^collaborator with userGuid post like feedback to incorrect item$")
    public void add_collaborator_feedback_to_incorrect_item_guid() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(addCollaboratorFeedback());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1)Check collaborator profile in customer:
        /api/customer/v1/profile/guestUser?userGuid=99902
        */

    }
    @Then("^post feedback to incorrect item status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("response contains invalid itemGuid error")
    public void  response_contains_invalid_feedback_error(){
        json.body("errors.error[0].developerMessage", equalTo("Invalid item guid"));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
