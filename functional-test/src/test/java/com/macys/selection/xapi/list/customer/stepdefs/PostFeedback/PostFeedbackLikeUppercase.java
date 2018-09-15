package com.macys.selection.xapi.list.customer.stepdefs.PostFeedback;

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

public class PostFeedbackLikeUppercase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostFeedbackLikeUppercase.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^collaborator with userGuid \"([^\"]*)\" post LIKE feedback for itemGuid \"([^\"]*)\" in the collaborative listGuid \"([^\"]*)\"$")
    public void collaborator_with_listGuid_and_feedback_LIKE(String userGuid, String itemGuid, String listGuid) {
        request = given().pathParams("itemGuid", itemGuid, "listGuid", listGuid).queryParams("userGuid", userGuid, "itemFeedback", "LIKE");
    }

    @When("^collaborator post LIKE feedback$")
    public void add_collaborator_feedback_LIKE() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(addCollaboratorFeedback());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1)Check collaborator profile in customer:
        /api/customer/v1/profile/guestUser?userGuid=99901
        2) Add feedback to collaborative list item
        /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99901/feedback/7dfa1ece-db69-4369-a222-99901-01?itemFeedback=LIKE&userGuid=99901&guestUser=false
        */

    }
    @Then("^post LIKE feedback by collaborator status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }
}
