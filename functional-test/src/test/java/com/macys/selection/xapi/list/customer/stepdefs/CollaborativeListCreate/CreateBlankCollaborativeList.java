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

public class CreateBlankCollaborativeList {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid$")
	  public void owner_user_with_userid() {
	    request = given();
	  }

	@When("^customer create a collaborative list$")
	  public void a_customer_create_collaborative_list_by_userGuid() {


	    String collaborativeList = "{\"list\":{\"name\":\"Test list 2\",\"userGuid\":\"90001001-90001001-90001001\"}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeList)
	            .post(getCollaborativeListEndpoint());
	    System.out.println("create list response " + response.prettyPrint());
	  }

	  @Then("^the create collaborative list status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains corresponding collaborative data")
	  public void response_equal() {
		  json.body("list.listGuid", equalTo("f338675e-c359-4de9-b083-9000100101"));
	  }
}
