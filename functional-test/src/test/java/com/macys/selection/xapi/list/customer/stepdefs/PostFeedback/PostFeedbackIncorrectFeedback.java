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

public class PostFeedbackIncorrectFeedback {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostFeedbackIncorrectFeedback.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^collaborator with userGuid \"([^\"]*)\" post incorrect feedback for itemGuid \"([^\"]*)\" in the collaborative listGuid \"([^\"]*)\"$")
    public void collaborator_with_listGuid_and_incorrect_feedback(String userGuid, String itemGuid, String listGuid) {
        request = given().pathParams("itemGuid", itemGuid, "listGuid", listGuid).queryParams("userGuid", userGuid, "itemFeedback", "liked");
    }

    @When("^collaborator with userGuid post incorrect feedback$")
    public void add_collaborator_incorrect_feedback() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(addCollaboratorFeedback());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1)Check collaborator profile in customer:
          /api/customer/v1/profile/guestUser?userGuid=99902
        2)Add incorrect feedback
          /api/selection/list/v1/collaborators/dfa1ece-db69-4369-a222-99901/feedback/7dfa1ece-db69-4369-a222-99901-10?itemFeedback=liked&userGuid=99901&guestUser=false
        */

    }
    @Then("^post incorrect feedback by collaborator status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("response contains invalid feedback error")
    public void  response_contains_invalid_feedback_error(){
        json.body("errors.error[0].developerMessage", equalTo("Invalid item feedback"));
        json.body("errors.error[0].errorCode", equalTo("50001"));
    }
}
