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

import static com.macys.selection.xapi.list.util.TestUtils.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;

public class DeleteCustomerListByBlankListGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerListByBlankListGuid.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with blank listguid \"([^\"]*)\"$")
    public void a_customer_with_blank_listguid(String listguid) {
        request = given().pathParam("listGuid", listguid);
    }

    @When("^customer tries to delete the list by blank listguid$")
    public void tries_to_delete_list_by_blank_listGuid() throws IOException {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(getListByGuidEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @Then("^the delete status code by blank listGuid is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

}
