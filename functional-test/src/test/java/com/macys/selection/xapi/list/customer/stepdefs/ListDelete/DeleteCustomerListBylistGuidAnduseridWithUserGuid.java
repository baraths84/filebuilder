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

public class DeleteCustomerListBylistGuidAnduseridWithUserGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerListBylistGuidAnduseridWithUserGuid.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer with listguid \"([^\"]*)\" and userid \"([^\"]*)\" with userGuid$")
    public void a_customer_with_listguid_and_userGuid(String listguid, String userid) {
        request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
    }

    @When("^customer tries to delete the list by listguid and userid with userGuid$")
    public void customer_tries_to_delete_list_with_userGuid() throws IOException {

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .delete(getListByGuidEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) To MSP Customer:
          GET /api/customer/v1/profile/guestUser?userid=201306333
        2) To MSP Wishlist:
          DELETE /api/selection/list/v1/wishlists/18c7557f-264c-490d-8e9e-956830d55622?userId=201306333&userGuid=18c7557f-264c-490d-8e9e-956830d55622&guestUser=false
         */
    }

    @Then("^the delete status code with userid with userGuid is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }


}
