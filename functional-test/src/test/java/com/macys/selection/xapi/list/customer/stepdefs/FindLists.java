package com.macys.selection.xapi.list.customer.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step definition file for find lists.
 **/
public class FindLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer finds list with firstname \"([^\"]*)\" and lastname \"([^\"]*)\" and state \"([^\"]*)\"$")
    public void a_customer_exits_with_first_and_last_name_and_state(String firstname, String lastname, String state) {
        request = given().queryParam("firstName", firstname).queryParam("lastName", lastname).queryParam("state", state);

    /*
    New flow:
    GET /api/customer/v2/profile/search?firstName=John&lastName=White&state=CA
    GET /api/selection/list/v1/wishlists?userIds=2150919011%2C2150919022%2C2150919033&userFirstName=John
     */
    }

    @And("^find lists with state check lists order and random user returned$")
    public void response_find_lists_equal() {
        json.body("CustomerList.user.guestUser", equalTo(false));
        json.body("CustomerList.user.profile.firstName", equalTo("John"));

        json.body("CustomerList.list[0].name", equalTo("List1"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].listGuid", equalTo("a78a1ed1-d7c0-4aae-55250005-2"));

        json.body("CustomerList.list[1].name", equalTo("List2"));
        json.body("CustomerList.list[1].defaultList", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[1].listType", equalTo("W"));
        json.body("CustomerList.list[1].numberOfItems", equalTo(1));
        json.body("CustomerList.list[1].listGuid", equalTo("a78a1ed1-d7c0-4aae-55250005-1"));

        json.body("CustomerList.list[2].name", equalTo("wishlist1"));
        json.body("CustomerList.list[2].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[2].listType", equalTo("W"));
        json.body("CustomerList.list[2].numberOfItems", equalTo(2));
        json.body("CustomerList.list[2].listGuid", equalTo("a78a1ed1-d7c0-4aae-55250006-1"));

    }

    @Given("^a customer finds list with firstname \"([^\"]*)\" and lastname \"([^\"]*)\"$")
    public void a_customer_exits_with_first_and_last_name(String firstname, String lastname) {
        request = given().queryParam("firstName", firstname).queryParam("lastName", lastname);
    }

    @Then("^the find list status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @When("^customer finds the list by first and last name$")
    public void a_customer_tries_to_find_list_by_first_and_last_name() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Given("^a customer finds list without firstname and with lastname \"([^\"]*)\"$")
    public void a_customer_finds_list_with_last_name(String lastName) {
        request = given().queryParam("lastName", lastName);
    }

    @And("^error response with missing firstname$")
    public void error_response_imissing_firstName() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("First Name is Missed"));
    }

    @Given("^a customer finds list without lastname and with firstname \"([^\"]*)\"$")
    public void a_customer_finds_list_with_first_name(String firstName) {
        request = given().queryParam("firstName", firstName);
    }

    @And("^error response with missing lastname$")
    public void error_response_missing_lastName() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Last Name is Missed"));
    }

    @And("^error response with lastname less than 2 chars$")
    public void error_response_lastName_1_char() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid Last Name: Min two characters."));
    }

    @Given("^a customer finds list with fields$")
    public void a_customer_finds_list_with_fields() {
        request = given().queryParam("firstName", "name").queryParam("lastName", "name").queryParam("fields", "fields");
    }

    @And("^error response incorrect request$")
    public void error_response_fields() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
    }

    @Given("^a customer finds list with expand")
    public void a_customer_finds_list_with_expand() {
        request = given().queryParam("firstName", "name").queryParam("lastName", "name").queryParam("expand", "expand");
    }

    @Given("^a customer finds list with userId$")
    public void a_customer_finds_list_with_userId() {
        request = given().queryParam("firstName", "name").queryParam("lastName", "name").queryParam("userId", "2233");
    }

    @Given("^a customer finds list with userGuid$")
    public void a_customer_finds_list_with_userGuid() {
        request = given().queryParam("firstName", "name").queryParam("lastName", "name").queryParam("userGuid", "111-222");
    }

}
