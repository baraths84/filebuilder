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

public class DeleteCustomerListByUserIdMIssedInCustomer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerListByUserIdMIssedInCustomer.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with listguid \"([^\"]*)\" and missed userid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_missed_userid(String listguid, String userid) {
        request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
    }

    @When("^customer tries to delete the list by missed userid$")
    public void a_customer_tries_to_delete_list_by_missed_userid() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .delete(getListByGuidEndpoint());
        LOGGER.info(response.prettyPrint());

    }

    @Then("^the delete status code with missed userid is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }


}
