package com.macys.selection.xapi.list.customer.stepdefs;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.core.MediaType;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

public class ShareListByEmail {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer is given a list with listGuid \"([^\"]*)\"")
    public void customer_is_given_a_list(String listGuid) {
        request = given().pathParam("listGuid", listGuid);
    }

    @When("^customer shares a list by email$")
    public void customer_shares_a_valid_list() throws JSONException {
        JSONObject EmailShare = new JSONObject()
                .put("EmailShare", new JSONObject()
                        .put("from", "")
                        .put("to", "qa15@test.com,qa16@test.com")
                        .put("message", "Test Message")
                        .put("link", "http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n")
                        .put("firstName", "first name")
                        .put("lastName", "last name"));
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(EmailShare.toString())
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .post(getShareEmailEndpoint());
        System.out.println("share list response" + response.prettyPrint());
    }

    @When("^customer shares a list with invalid input _to_ by email$")
    public void customer_shares_a_invalid_to_list() throws JSONException {
        JSONObject EmailShare = new JSONObject()
                .put("EmailShare", new JSONObject()
                        .put("from", "")
                        .put("to", "1qa15@test.com,qa16@test.com")
                        .put("message", "Test Message")
                        .put("link", "http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n")
                        .put("firstName", "first name")
                        .put("lastName", "last name"));
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(EmailShare.toString())
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .post(getShareEmailEndpoint());
        System.out.println("share list response" + response.prettyPrint());
    }

    @When("^customer shares a list with invalid input _message_ by email$")
    public void customer_shares_a_invalid_message_list() throws JSONException {
        JSONObject EmailShare = new JSONObject()
                .put("EmailShare", new JSONObject()
                        .put("from", "")
                        .put("to", "qa15@test.com,qa16@test.com")
                        .put("message", "1Test Message")
                        .put("link", "http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n")
                        .put("firstName", "first name")
                        .put("lastName", "last name"));
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(EmailShare.toString())
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .post(getShareEmailEndpoint());
        System.out.println("share list response" + response.prettyPrint());
    }

    @Then("^the sharing list by email status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^The error response contains an error message \"([^\"]*)\" and errorCode \"([^\"]*)\"$")
    public void verify_error_code(String errorMessage, String errorCode) {
        json.body("errors.error[0].developerMessage", equalTo(errorMessage));
        json.body("errors.error[0].errorCode", equalTo(errorCode));
    }

}



