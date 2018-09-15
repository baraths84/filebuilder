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

public class CreateCollaborativeListBlankBody {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and list with blank body$")
	  public void owner_user_with_userid_create_list_with_blank_body() {
	    request = given();
	  }

	@When("^customer create a collaborative list with blank body$")
	  public void a_customer_create_collaborative_list_with_blank_body() {


	    String collaborativeList = "{}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeList)
	            .post(getCollaborativeListEndpoint());
	    System.out.println("create list response " + response.prettyPrint());
	  }

	  @Then("^the create collaborative list with blank body status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains error about blank body")
	  public void response_equal()
	  {
		  json.body("errors.error[0].message", equalTo("Couldn't deserialize object"));
		  json.body("errors.error[0].errorCode", equalTo("02018"));
	  }
}
