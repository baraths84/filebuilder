package com.macys.selection.xapi.list.customer.stepdefs.AvatarGet;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getUserAvatarByUserGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetAvatarNullResponse {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^a customer get null avatar image response by userGuid \"([^\"]*)\"$")
	  public void user_with_userGuid_and_avatar(String userGuid) {
	    request = given().pathParams("userGuid", userGuid);
	  }

	@When("^customer get null avatar image response$")
	public void a_customer_create_avatar_by_userGuid() {

		response = request
				.contentType(MediaType.APPLICATION_JSON)
				.when()
				.get(getUserAvatarByUserGuidEndpoint());
		System.out.println("create list response " + response.prettyPrint());
	}

	@Then("^get null avatar image status code is \"([^\"]*)\"$")
	public void verify_status_code(int statusCode) {
		json = response.then().statusCode(statusCode);
	}

	@And("^response contains null avatar image data$")
	public void error_response_equal() {
		json.body("profilePicture.avatar", equalTo(null));
	}
}
