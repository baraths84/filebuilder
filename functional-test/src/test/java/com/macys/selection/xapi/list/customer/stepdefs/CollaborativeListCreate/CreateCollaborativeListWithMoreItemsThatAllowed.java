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

public class CreateCollaborativeListWithMoreItemsThatAllowed {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and big list of items$")
	  public void owner_user_with_userid_and_list_itemss() {
	    request = given();
	  }

	@When("^customer create a collaborative list with a lot of items$")
	  public void a_customer_create_collaborative_list_items() {

	    String collaborativeListWithItems = "{\"list\":{\"name\": \"List with a lot of items\",\"userGuid\": \"90001005-90001005-90001005\",\"items\":[{\"upc\":{\"id\": 90001}},{\"upc\":{\"id\":90002}},{\"upc\":{\"id\":90003}},{\"upc\":{\"id\":90004}},{\"upc\":{\"id\":90005}},{\"upc\":{\"id\":90006}},{\"upc\":{\"id\":90007}}]}}\n";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
				.post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	@Then("^the create collaborative list with a lot of items status code is \"([^\"]*)\"$")
	public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	@And("^response contains error about exceeded items number$")
	public void response_equal_collaborative_list_with_items() {
		json.body("errors.error[0].developerMessage", equalTo("Maximum Items per list reached"));
		json.body("errors.error[0].errorCode", equalTo("50001"));
	}
}



