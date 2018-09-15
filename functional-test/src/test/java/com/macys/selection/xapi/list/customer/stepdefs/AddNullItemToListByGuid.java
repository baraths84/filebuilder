package com.macys.selection.xapi.list.customer.stepdefs;

import com.macys.selection.xapi.list.util.TestUtils;
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

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddNullItemToListByGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddNullItemToListByGuid.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer adds null item to existing list with listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid(String listguid) {
        request = given().pathParam("listGuid", listguid);
    }

    @When("^customer adds a null item to the existing list$")
    public void a_customer_adds_null_item_to_default_list() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addNullItemToListByGuid.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getItemsByListGuidEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^the add null item to existing list status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^i verify the add null item to existing list response$")
    public void error_response_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Bad Json from UI, at least one item is required!"));
    }
}
