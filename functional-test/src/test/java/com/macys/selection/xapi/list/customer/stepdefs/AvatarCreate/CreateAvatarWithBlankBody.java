package com.macys.selection.xapi.list.customer.stepdefs.AvatarCreate;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getUserAvatarEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateAvatarWithBlankBody {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^a customer add avatar image with blank body$")
	  public void user_with_userGuid_and_avatar() {
	    request = given();
	  }

	@When("^customer add avatar image with blank body$")
	  public void a_customer_create_avatar_by_userGuid() {

		String avatar = "{\"profilePicture\": {}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(avatar)
	            .post(getUserAvatarEndpoint());
	    System.out.println("create list response " + response.prettyPrint());
	  }

	  @Then("^create avatar image with blank body status code is \"([^\"]*)\"$")
	  public void verify_status_code(int statusCode) {
	    json = response.then().statusCode(statusCode);
	  }

	@And("^response contains blank avatar body error$")
	public void response_contains_incorrect_userGuid_error() {
		json.body("errors.error[0].errorCode", equalTo("50001"));
		json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
	}
}
