package com.macys.selection.xapi.list.customer.stepdefs.ListDelete;

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

public class DeleteCustomerListByListguidWithNullUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerListByListguidWithNullUser.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with null user and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid(String listguid) {
        request = given().pathParam("listGuid", listguid);
    }

    @When("^customer tries to delete the list by listguid and null user$")
    public void a_customer_adds_item_to_default_list() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getListByGuidEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^the delete status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

}
