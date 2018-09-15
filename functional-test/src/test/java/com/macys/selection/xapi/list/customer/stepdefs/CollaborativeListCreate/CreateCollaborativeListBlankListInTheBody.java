package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListCreate;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCollaborativeListEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCollaborativeListBlankListInTheBody {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and list with blank list")
	  public void owner_user_with_userid_create_list_with_blank_list() {
	    request = given();
	  }

	@When("^customer create a collaborative list with blank list")
	  public void a_customer_create_collaborative_list_with_blank_list() {


	    String collaborativeList = "{\"list\":{}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeList)
	            .post(getCollaborativeListEndpoint());
	    System.out.println("create list response " + response.prettyPrint());
	  }

	  @Then("^the create collaborative list with blank list status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains error about blank list")
	  public void response_equal()
	  {
		  json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
		  json.body("errors.error[0].errorCode", equalTo("50001"));
	  }
}
